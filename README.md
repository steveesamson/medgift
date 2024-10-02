About

# MedGift

MedGift is a service designed to assist people living far from their loved ones in managing healthcare. It allows users to gift medical care to family members, track their health, and pay for these services remotely, offering a way to gift medical support.

## Workflow steps

1. update main branch locally

```shell
git pull origin main
```

2. create a branch for your work locally from the main branch

```shell
git checkout -b <branch-name>
```

3. open code in your IDE and work

4. When done with change commit and push to the remote repo

```shell
git add -A
git commit -m "Commit message" # e.g "TicketID: Ticket description"
git push -u origin <branch-name> # as chosen earlier

```

5. go to medgift on github and create a pull request for your work/branch. Assign reviewers to review and inform them.
6. when approved, merge to main
7. other fix changes as commented

In cases of code conflicts,

1. switch to the main branch
2. update your local main
3. switch to your work branch and do a rebase against the main branch

```shell
git rebase main
```

TO KNOW

```shell
git rebase --continue
git rebase --abort
```
