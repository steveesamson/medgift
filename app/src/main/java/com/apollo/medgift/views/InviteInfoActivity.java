package com.apollo.medgift.views;

import android.content.Intent;
import android.os.Bundle;

import com.apollo.medgift.R;
import com.apollo.medgift.common.BaseActivity;
import com.apollo.medgift.common.Closeable;
import com.apollo.medgift.common.Firebase;
import com.apollo.medgift.common.Util;
import com.apollo.medgift.databinding.ActivityInviteinfoBinding;
import com.apollo.medgift.models.Gift;
import com.apollo.medgift.models.GiftInvite;
import com.apollo.medgift.models.HealthcareService;
import com.apollo.medgift.models.Recipient;
import com.apollo.medgift.models.User;

public class InviteInfoActivity extends BaseActivity {
    private ActivityInviteinfoBinding binding;
    private Recipient recipient;
    private Gift gift;
    private User giftOwner;
    private GiftInvite giftInvite;
    private Closeable closeable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applyWindowInsetsListenerTo(this, binding.main);
        // Setup tool bar and title
        setupToolbar(binding.homeAppBar.getRoot(), getString(R.string.invitationDetails), true);

        Intent intent = getIntent();
        giftInvite = (GiftInvite) intent.getSerializableExtra(GiftInvite.STORE);
        assert giftInvite != null;

        Util.startProgress(binding.progress, "Loading data...");


            closeable = Firebase.getModelBy(Gift.STORE, "key", giftInvite.getGiftId(), Gift.class, (gft) -> {
                gift = gft;
                if(gift != null){
                    binding.giftTitle.setText(gift.getName());
                    binding.txtGiftDescription.setText(gift.getDescription());
                    setupActivity();
                }
                closeable.release();

            });
    }


    private void setupActivity() {

        assert gift != null;
        closeable = Firebase.getModelBy(Recipient.STORE, "key", gift.getRecipientId(), Recipient.class, (recp) -> {
            recipient = recp;
            closeable.release();
            if(recipient != null){
                binding.txtRecipient.setText(recipient.toString());
            }
            closeable = Firebase.getModelBy(User.STORE, "key", gift.getCreatedBy(), User.class, (gftOwner) -> {
                giftOwner = gftOwner;
                closeable.release();
                if(giftOwner != null){
                    binding.txtGifterName.setText(String.format("%s %s <%s>", giftOwner.getFirstName(), giftOwner.getLastName(), giftOwner.getEmail()));
                }
            });

        });
        Util.stopProgress(binding.progress);
        binding.btnContribute.setOnClickListener((v) -> {
            Intent intent = new Intent(this, HealthcareService.class);
            intent.putExtra(Gift.STORE, gift);
            startActivity(intent);
        });



    }
}