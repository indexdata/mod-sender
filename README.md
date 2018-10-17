# mod-sender

Copyright (C) 2018 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

# Introduction

The module acts as an intermediary that sorts the prepared messages, sends them through the appropriate delivery channels. All technical
complexity, sending queues, persistence of messages, available sending channels will be regulated by this module. Module provide single
REST endpoint - POST /message-delivery for delivering message by using available delivery channels.

# Additional information

The [raml-module-builder](https://github.com/folio-org/raml-module-builder) framework.

Other [modules](https://dev.folio.org/source-code/#server-side).

Other FOLIO Developer documentation is at [dev.folio.org](https://dev.folio.org/)
