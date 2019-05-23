# mod-sender

Copyright (C) 2018-2019 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

# Introduction

Sending messages through delivery channels

The module acts as an intermediary that sorts the prepared messages, sends them through the appropriate delivery channels. All technical
complexity, sending queues, persistence of messages, available sending channels are regulated by this module. Module provide single
REST endpoint - POST /message-delivery for delivering message by using available delivery channels.

**POST /message-delivery**  
**Required permission:** "sender.message-delivery"

Request body example:
```
{
  "notificationId": "c276e2ed-cc4b-4e3f-ad36-91aa3a4f4c2a",
  "recipientUserId": "33c59afd-9970-4205-9198-84eba036d105",
  "messages": [
    {
      "deliveryChannel": "email",
      "header": "Your FOLIO password changed",
      "body": "Dear Alex,\n\nYour password has been changed.",
      "outputFormat": "text/plain"
    },
    {
      "deliveryChannel": "sms",
      "header": "Your FOLIO password changed",
      "body": "Dear Alex, Your password has been changed.",
      "outputFormat": "text/plain"
    }
  ]
}
```

# Additional information

The [raml-module-builder](https://github.com/folio-org/raml-module-builder) framework.

Other [modules](https://dev.folio.org/source-code/#server-side).

See project [MODSENDER](https://issues.folio.org/browse/MODSENDER) at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

Other FOLIO Developer documentation is at [dev.folio.org](https://dev.folio.org/)
