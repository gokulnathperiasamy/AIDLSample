package com.kpgn.aidlsample.mathserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.kpgn.aidlsample.IMathService;
import com.kpgn.aidlsample.mathserver.common.OperationType;


public class MathService extends Service {
    public MathService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IMathService.Stub binder = new IMathService.Stub() {
        @Override
        public double mathOperation(double firstNumber, double secondNumber, String operation) throws RemoteException {
            switch (operation) {
                case OperationType.OP_ADD:
                    return firstNumber + secondNumber;
                case OperationType.OP_SUB:
                    return firstNumber - secondNumber;
                case OperationType.OP_MUL:
                    return firstNumber * secondNumber;
                case OperationType.OP_DIV:
                    if (secondNumber != 0) {
                        return firstNumber / secondNumber;
                    }
            }
            return 0;
        }
    };

}
