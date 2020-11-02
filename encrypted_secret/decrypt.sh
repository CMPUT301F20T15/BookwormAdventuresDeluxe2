#!/bin/sh

# Decrypt the file
# --batch to prevent interactive command
# --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$ENCRYPTION_KEY_PASSWORD" \
--output $HOME/work/BookwormAdventuresDeluxe2/BookwormAdventuresDeluxe2/encrypted_secret/secrets.xml.gpg \
$HOME/work/BookwormAdventuresDeluxe2/BookwormAdventuresDeluxe2/app/src/main/java/res/values/secrets.xml