package com.realvibedev.paoquente.Service;

import com.realvibedev.paoquente.Common;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by bruno on 29/03/2018.
 */

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Common.currentToken = mToken;
    }
}
