package com.kpgn.aidlsample.calculatorapp.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kpgn.aidlsample.IMathService;
import com.kpgn.aidlsample.calculatorapp.R;
import com.kpgn.aidlsample.calculatorapp.common.OperationType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculatorActivity extends AppCompatActivity {

    @BindView(R.id.etFirstNumber)
    EditText mFirstNumber;

    @BindView(R.id.etSecondNumber)
    EditText mSecondNumber;

    @BindView(R.id.tvResult)
    TextView mResult;

    private String operation = OperationType.OP_ADD;
    private IMathService mathService = null;
    private ServiceConnection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        ButterKnife.bind(this);
        if (mathService == null) {
            createConnection();
            Intent intent = new Intent("com.kpgn.aidlsample.mathserver.service.MathService");
            intent.setPackage("com.kpgn.aidlsample.mathserver");
            //startService(intent);
            boolean isBound = bindService(intent, connection, Context.BIND_AUTO_CREATE);
            isBound = isBound;
            //startService(new Intent("com.kpgn.aidlsample.mathserver.service.MathService"));
        }
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.r_add, R.id.r_sub, R.id.r_mul, R.id.r_div})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        int id = radioButton.getId();
        if (checked) {
            if (id == R.id.r_add) {
                operation = OperationType.OP_ADD;
            } else if (id == R.id.r_sub) {
                operation = OperationType.OP_SUB;
            } else if (id == R.id.r_mul) {
                operation = OperationType.OP_MUL;
            } else if (id == R.id.r_div) {
                operation = OperationType.OP_DIV;
            }
        } else {
            operation = OperationType.OP_ADD;
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.bCalculate)
    public void ctaCalculate(View view) {
        try {
            double firstNumber = Double.parseDouble(mFirstNumber.getText().toString());
            double secondNumber = Double.parseDouble(mSecondNumber.getText().toString());
            double result = mathService.mathOperation(firstNumber, secondNumber, operation);
            mResult.setText(String.valueOf(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createConnection() {
        connection = new ServiceConnection() {

            @Override
            public void onNullBinding(ComponentName name) {
                Toast.makeText(getApplicationContext(), "Service Null", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBindingDied(ComponentName name) {
                Toast.makeText(getApplicationContext(), "Service Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mathService = IMathService.Stub.asInterface(service);
                Toast.makeText(getApplicationContext(), "Service Connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mathService = null;
                Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
