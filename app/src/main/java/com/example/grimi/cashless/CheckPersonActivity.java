package com.example.grimi.cashless;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CheckPersonActivity extends AppCompatActivity {
    private static final String TAG = "CashLess Example";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AS2aL6TvAOVt0rbuI-NXMKlDk4SjcnfcaxnfjhkVO2DBTwOMFVH1coV9bLktbBiwI9ZtJsrxdj7NxCog";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    ArrayList<String> currentCheck = new ArrayList<String>();
    ArrayAdapter<String> itemsAdapter;
    ListView checkListView;

    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_personal);
        Button checkout = (Button)findViewById(R.id.button_checkout);
        Bundle extras = getIntent().getExtras();
        currentCheck = extras.getStringArrayList("items");
        total = extras.getInt("subtotal");

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currentCheck);
        checkListView = (ListView) findViewById(R.id.personCheck);
        checkListView.setAdapter(itemsAdapter);

        TextView subtotal = (TextView) findViewById(R.id.subtotal);
        subtotal.setText("Subtotal: $" + Integer.toString(total));

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    public void onBuyPressed(View pressed) {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(CheckPersonActivity.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        EditText propina = (EditText) findViewById(R.id.propina);
        int the_total = (total * Integer.parseInt(String.valueOf(propina.getText()))/100) + total;
        return new PayPalPayment(new BigDecimal(the_total), "MXN", "Monto a pagar: ",
                paymentIntent);
    }
}
