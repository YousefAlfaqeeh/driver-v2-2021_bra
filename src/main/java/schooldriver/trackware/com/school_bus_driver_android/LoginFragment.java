package schooldriver.trackware.com.school_bus_driver_android;

import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import schooldriver.trackware.com.school_bus_driver_android.basePage.BaseFragment;
import schooldriver.trackware.com.school_bus_driver_android.interfaceDriver.IActionLogin;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.StaticValue;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilViews;
import schooldriver.trackware.com.school_bus_driver_android.utilityDriver.UtilityDriver;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends BaseFragment implements IActionLogin {

    Button btnLogin;
    View btnLanguage;
    EditText txtDriverCode;
    LoginPresenter mPresenter;
    String pin;
//    private SwitchCompat dev_prod_Switch;

    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mPresenter = new LoginPresenter(getMainActivity(), LoginFragment.this);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLanguage = view.findViewById(R.id.btnLanguage);
        txtDriverCode = (EditText) view.findViewById(R.id.txtDriverCode);
//        dev_prod_Switch = (SwitchCompat) view.findViewById(R.id.dev_prod_Switch);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilityDriver.getStringShared(UtilityDriver.LANGUAGE).equals("ar")) {
                    UtilityDriver.setStringShared(UtilityDriver.LANGUAGE, "en");
                    UtilityDriver.setLocale("en", getMainActivity(), MainActivity.class);
                } else {
                    UtilityDriver.setStringShared(UtilityDriver.LANGUAGE, "ar");
                    UtilityDriver.setLocale("ar", getMainActivity(), MainActivity.class);
                }
            }
        });
        StaticValue.loginFragment = LoginFragment.this;

//        jusForTesting();
        return view;
    }



//  private  void  jusForTesting(){
//      dev_prod_Switch.setOnLongClickListener(new View.OnLongClickListener() {
//          @Override
//          public boolean onLongClick(View view) {
//              view.setVisibility(View.GONE);
//              return true;
//          }
//      });
//        dev_prod_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (dev_prod_Switch.isChecked()) {
//                    dev_prod_Switch.setText("Dev");
//                    UtilityDriver.setBooleanShared("isDev", true);
//                    PathUrl.MAIN_URL = PathUrl.DIV_URL;
//                } else {
//                    dev_prod_Switch.setText("Prod");
//                    UtilityDriver.setBooleanShared("isDev", false);
//                    PathUrl.MAIN_URL = PathUrl.PROD_URL;
//                }
//            }
//        });
//        if (UtilityDriver.getBooleanShared("isDev")) {
//            dev_prod_Switch.setText("Dev");
//            UtilityDriver.setBooleanShared("isDev", true);
//            PathUrl.MAIN_URL = PathUrl.DIV_URL;
//            dev_prod_Switch.setChecked(true);
//        } else {
//            dev_prod_Switch.setText("Prod");
//            UtilityDriver.setBooleanShared("isDev", false);
//            PathUrl.MAIN_URL = PathUrl.PROD_URL;
//            dev_prod_Switch.setChecked(true);
//        }
//
//    }

    private void login() {

        String driverCode = txtDriverCode.getText().toString().trim().toUpperCase();
        if (UtilityDriver.isEmptyString(driverCode)) {
            UtilViews.shakeViews(txtDriverCode);
            UtilityDriver.showMessage(getMainActivity(), mActivity.getString(R.string.please_enter_driver_code));
            return;

        }
        if (StaticValue.progressDialog != null) {
            StaticValue.progressDialog.show();
        }
        pin = driverCode;
        txtDriverCode.setText("");
        mPresenter.callApiLogin(driverCode, "bus_pin");
    }


    @Override
    public void done(String message) {
        UtilityDriver.showMessageDialog(getMainActivity(), mActivity.getString(R.string.success), message);
        txtDriverCode.setVisibility(View.VISIBLE);
//        UtilityDriver.setStringShared(UtilityDriver.PIN,pin);
    }

    @Override
    public void error(String message) {
        UtilViews.shakeViews(txtDriverCode);
        UtilityDriver.showMessageDialog(getMainActivity(), mActivity.getString(R.string.failed), message);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getMainActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        StaticValue.loginFragment = null;
    }


}
