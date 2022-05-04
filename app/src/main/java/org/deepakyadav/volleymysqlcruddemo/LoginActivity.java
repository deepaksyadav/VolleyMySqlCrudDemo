package org.deepakyadav.volleymysqlcruddemo;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {
    TextView registerTV, msgTV;
    EditText user, password;
    Button loginBtn;
    private RequestQueue rQueue;
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerTV = findViewById(R.id.registerTV);
        msgTV= findViewById(R.id.msgTV);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAction();
            }
        });
    }
    private void loginAction() {
        final String userr = user.getText().toString();
        final String pswd = password.getText().toString();
        if (userr.isEmpty()) {
            user.setError("Username or Email is required");
            user.requestFocus();
            return;
        }
        if (pswd.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        Toast.makeText(LoginActivity.this, "before STRING REQ! ", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "login.php",
                response -> {
                    rQueue.getCache().clear();
                    Toast.makeText(LoginActivity.this, "get CACHE! ", Toast.LENGTH_SHORT).show();
                    Log.e("anyText",response);
                    try {
                        Toast.makeText(LoginActivity.this, "in TRY! ", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.optString("success").equals("1")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                            sharedPrefrencesHelper.setFirstname(jsonObject1.getString("firstname"));
                            sharedPrefrencesHelper.setLastname(jsonObject1.getString("lastname"));
                            sharedPrefrencesHelper.setUsername(jsonObject1.getString("username"));
                            sharedPrefrencesHelper.setEmail(jsonObject1.getString("email"));

                            Toast.makeText(LoginActivity.this, "Login Successfully! ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", userr);
                params.put("password", pswd);
                return params;
            }
            
        };
        //String sR= stringRequest.toString();
        String sR= userr;

        msgTV.setText(sR);
        rQueue = Volley.newRequestQueue(LoginActivity.this);
        rQueue.add(stringRequest);
    }
}