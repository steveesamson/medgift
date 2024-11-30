package com.apollo.medgift.views.gifter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apollo.medgift.R;
import com.apollo.medgift.adapters.gifters.ContributorAdapter;
import com.apollo.medgift.adapters.gifters.InviteeAdapter;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.BaseModel;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.OnModelDeleteCallback;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.common.ValueEvents;
import com.apollo.medgift.databinding.ActivityAddgiftBinding;
import com.apollo.medgift.databinding.ContributorDialogBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.GiftService;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.SessionUser;
import com.apollo.medgift.models.User;
import com.apollo.medgift.views.models.GiftInviteVModel;
import com.apollo.medgift.views.models.GiftServiceVModel;
import com.apollo.medgift.views.provider.ServiceActivity;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddGiftActivity extends BaseActivity implements View.OnClickListener, OnModelDeleteCallback {
    private static final  String TAG = AddGiftActivity.class.getSimpleName();

    private ActivityAddgiftBinding addGiftBinding;
    private ContributorAdapter contributorAdapter;
    private InviteeAdapter inviteeAdapter;
    private ValueEventListener inviteesListener;
    private ValueEventListener contributionsListener;
    private Query inviteeQuery;
    private Query contributorQuery;


    private Gift gift;
    private final List<GiftInvite> dirtyInvitees = new ArrayList<>();
    private final List<GiftInvite> invitees = new ArrayList<>();
    private final List<GiftService> contributors = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        addGiftBinding = ActivityAddgiftBinding.inflate(getLayoutInflater());
        setContentView(addGiftBinding.getRoot());
        applyWindowInsetsListenerTo(this, addGiftBinding.addGift);
        Intent intent = getIntent();
        gift = (Gift) intent.getSerializableExtra(Gift.STORE);
        boolean exists = Util.exists(gift);
        String title = getString(exists ? R.string.editGiftTitle : R.string.addGiftTitle);
        setupToolbar(addGiftBinding.homeAppBar.getRoot(), title, true);
        if (!exists) {
            SessionUser sessionUser = Firebase.currentUser();
            assert sessionUser != null;
            gift.setCreatedBy(sessionUser.getUserId());
        }

        setup();

    }

    private void setup() {

        addGiftBinding.edtGiftName.setText(gift.getName());
        addGiftBinding.acRecipient.setText(gift.getRecipientId());
        addGiftBinding.edtGiftDescription.setText(gift.getDescription());

        addGiftBinding.isGroupGift.setOnCheckedChangeListener((buttonView, isChecked) -> {
                gift.setIsGroup(isChecked);
                addGiftBinding.contributorsLyt.setVisibility(isChecked? View.VISIBLE : View.GONE);
        });

        addGiftBinding.inviteContributors.setOnClickListener(this);
        addGiftBinding.btnSave.setOnClickListener(this);
        if(!Util.isNullOrEmpty(this.gift.getKey())){
            addGiftBinding.btnDeleteGift.setOnClickListener(this);
            addGiftBinding.btnDeleteGift.setVisibility(View.VISIBLE);
            addGiftBinding.btnAddService.setOnClickListener(this);
            addGiftBinding.btnAddService.setVisibility(View.VISIBLE);
        }

        addGiftBinding.contributorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contributorAdapter = new ContributorAdapter(this, contributors);
        addGiftBinding.contributorsRecyclerView.setAdapter(contributorAdapter);
        contributorQuery = Firebase.database(GiftService.STORE).orderByChild("giftId").equalTo(gift.getKey());

        addGiftBinding.inviteesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inviteeAdapter = new InviteeAdapter(this, invitees);
        addGiftBinding.inviteesRecyclerView.setAdapter(inviteeAdapter);
        inviteeQuery = Firebase.database(GiftInvite.STORE).orderByChild("giftId").equalTo(gift.getKey());
        addGiftBinding.contributorsLyt.setVisibility(this.gift.getIsGroup()? View.VISIBLE : View.GONE);
        addGiftBinding.isGroupGift.setChecked(this.gift.getIsGroup());

        setUpRecipientsDropdown();
        updateRecyclers();
    }

    private void setUpRecipientsDropdown() {
        SessionUser sessionUser = Firebase.currentUser();
        assert sessionUser != null;
        Firebase.getModelsBy(Recipient.STORE,"createdBy", sessionUser.getUserId(), Recipient.class, (_recipients) -> {
            if (_recipients != null) {
                Recipient[] recipients = _recipients.toArray(new Recipient[]{});
                ArrayAdapter<Recipient> recipientAdapter = new ArrayAdapter<Recipient>(AddGiftActivity.this, R.layout.recipient_acitem, recipients);
                addGiftBinding.acRecipient.setAdapter(recipientAdapter);
                addGiftBinding.acRecipient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Recipient r = (Recipient) parent.getItemAtPosition(position);
                        gift.setRecipientId(r.getKey());
                        addGiftBinding.acRecipient.setText(String.format("%s %s", r.getFirstName(), r.getLastName()), false);
                    }
                });
                Optional<Recipient> result = _recipients
                        .stream()
                        .parallel()
                        .filter(next -> next.getKey().equals(gift.getRecipientId())).findAny();
                if(result.isPresent()){
                    Recipient r = result.get();
                    addGiftBinding.acRecipient.setText(String.format("%s %s", r.getFirstName(), r.getLastName(), r.getEmail()));
                }
            }
        });
    }

    private void clearErrors() {
        addGiftBinding.edtGiftName.setError("");
        addGiftBinding.acRecipient.setError("");
        addGiftBinding.edtGiftDescription.setError("");
    }

    private void saveGift() {
        clearErrors();

        String giftName = Util.valueOf(addGiftBinding.edtGiftName);
        String giftDescription = Util.valueOf(addGiftBinding.edtGiftDescription);

        boolean formIsValid = true;

        if (Util.isNullOrEmpty(giftName)) {
            addGiftBinding.lytGiftName.setError("Gift name is required.");
            formIsValid = false;
        }
        if (Util.isNullOrEmpty(giftDescription)) {
            addGiftBinding.lytGiftDescription.setError("Gift description is required.");
            formIsValid = false;
        }

        if (gift.getIsGroup() && invitees.isEmpty()) {
            Util.notify(AddGiftActivity.this, "A group gift must have at least an invited contrubutor.");
            formIsValid = false;
        }

        if (Util.isNullOrEmpty(gift.getRecipientId())) {
            addGiftBinding.lytRecipient.setError("Recipient is required.");
            formIsValid = false;
        }

        if (formIsValid) {
            gift.setName(giftName);
            gift.setDescription(giftDescription);

            boolean exists = Util.exists(gift);

            Util.startProgress(addGiftBinding.progress, "Adding Gift...");

            Firebase.save(gift, Gift.STORE, (task, key) -> {
                Util.stopProgress(addGiftBinding.progress);
                if (task.isSuccessful()) {
                    gift = null;
                    for(GiftInvite gi: dirtyInvitees){
                        gi.setGiftId(key);
                        gi.setCreationDate(Util.today());
                        Firebase.save(gi, GiftInvite.STORE, (tk, id) ->{});
                    }
                    Util.notify(AddGiftActivity.this, Util.success("Gift", exists));
                    finish();

                } else {
                    Util.notify(AddGiftActivity.this, Util.fail("Gift", exists));
                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        if (view == addGiftBinding.btnSave) {
            saveGift();
        } else if (view == addGiftBinding.inviteContributors) {
            showInviteContributorsDialog();
        }
        else if(view == addGiftBinding.btnDeleteGift){
            // Delete here
                Util.showConfirm(this, "Delete", "Do your really want to delete this Gift?", (dialog, which) -> {
                    // Implement delete
                    Firebase.delete(gift, Gift.STORE,(task) -> {
                        if(task.isSuccessful()){
                            Util.notify(AddGiftActivity.this, "Gift deleted!");
                            finish();
                        }
                    });
                    dialog.dismiss();
                });
        } else if(view == addGiftBinding.btnAddService){
            Intent intent = new Intent(this, ServiceActivity.class);
            intent.putExtra(Gift.STORE, this.gift);
            startActivity(intent);
        }
    }


    private void showInviteContributorsDialog() {
        ContributorDialogBinding dialogBinding = ContributorDialogBinding.inflate(getLayoutInflater());
        showIDialogFor(dialogBinding.getRoot(), "Invite Contributor", "Invite", (dialog, which) -> {
            String email = Util.valueOf(dialogBinding.edtContributorEmail);
            SessionUser sessionUser = Firebase.currentUser();
            assert sessionUser != null;
            if (!email.isEmpty() && Util.isEmail(email) && !email.equals(sessionUser.getEmail())) {
                Firebase.getModelBy(User.STORE, "email", email, User.class, (user) -> {
                    dialogBinding.txtSuccess.setVisibility(View.GONE);
                    dialogBinding.txtError.setVisibility(View.GONE);
                    if (user == null) {
                        dialogBinding.txtError.setText(R.string.user_with_email_not_found);
                        dialogBinding.txtError.setVisibility(View.VISIBLE);
                    } else {
                        GiftInvite invite = new GiftInvite();
                        invite.setGiftId(gift.getKey());
                        invite.setEmail(email);
                        invite.setName(String.format("%s %s", user.getFirstName(), user.getLastName()));
                        invitees.add(invite);
                        dirtyInvitees.add(invite);
                        inviteeAdapter.notifyDataSetChanged();
                        addGiftBinding.emptyItemInvitees.getRoot().setVisibility(View.GONE);
                        dialogBinding.txtSuccess.setText(R.string.user_successfull_added);
                        dialogBinding.edtContributorEmail.setText("");
                        Util.notify(this, "Invitee " + email + ", added.");
                        dialog.dismiss();
                    }
                });

            } else {
                Util.notify(this, getString(R.string.provide_a_valid_email));
            }
        });

    }

    private void updateRecyclers() {

        if (contributorQuery != null && inviteeQuery != null) {
            Util.startProgress(addGiftBinding.progress, "Fetching contributors and invitees...");
            GiftServiceVModel contributorVModel = new ViewModelProvider(this).get(GiftServiceVModel.class);
            ValueEvents<GiftService> valueEvents = new ValueEvents<GiftService>();

            contributionsListener = valueEvents.registerListener(contributorQuery, this, contributorAdapter, contributorVModel, contributors, GiftService.class, (list) -> {
//                Util.stopProgress(addGiftBinding.progress);
                addGiftBinding.emptyItemContributors.txtEmpty.setText(list.isEmpty() ? "No contributors" : "");
                addGiftBinding.emptyItemContributors.getRoot().setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });


             GiftInviteVModel inviteVModel = new ViewModelProvider(this).get(GiftInviteVModel.class);
            ValueEvents<GiftInvite> ivalueEvents = new ValueEvents<GiftInvite>();

            inviteesListener = ivalueEvents.registerListener(inviteeQuery, this, inviteeAdapter, inviteVModel, invitees, GiftInvite.class, (list) -> {
                Util.stopProgress(addGiftBinding.progress);
                addGiftBinding.emptyItemInvitees.txtEmpty.setText(list.isEmpty() ? "No invitees" : "");
                addGiftBinding.emptyItemInvitees.getRoot().setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });
        }

    }



    // Unregister listeners on db
    private void unRegisterValueListener() {
        if (inviteesListener != null) {
            this.inviteeQuery.removeEventListener(inviteesListener);
        }
        if(contributionsListener != null){
            this.contributorQuery.removeEventListener(contributionsListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterValueListener();
    }


    @Override
    public void onDeleting(BaseModel model) {
        GiftInvite gi = (GiftInvite) model;
        dirtyInvitees.remove(gi);
        invitees.remove(gi);
        inviteeAdapter.notifyDataSetChanged();
    }
}