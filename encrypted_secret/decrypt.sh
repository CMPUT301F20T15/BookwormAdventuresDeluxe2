#!/bin/sh

# Decrypt the file
# --batch to prevent interactive command
# --yes to assume "yes" for questions
ls
cd app
ls
cd src
ls
cd main
ls
cd java
ls
cd res
ls
cd values
ls
gpg --quiet --batch --yes --decrypt --passphrase="$ENCRYPTION_KEY_PASSWORD" \
--output ./app/src/main/java/res/values/secrets.xml \
$HOME/work/BookwormAdventuresDeluxe2/BookwormAdventuresDeluxe2/encrypted_secret/secrets.xml.gpg