package it.androidworld.devcorner.qrgenerator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class MainActivity extends Activity {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF33B5E5;

    private TextView txtMessage;
    private ImageView imgQR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtMessage.setError(null);
            }
        });
        imgQR = (ImageView) findViewById(R.id.imgQRCode);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_generate_qr) {
            checkString();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkString() {

        final String message = txtMessage.getText().toString();

        if ((null != message) && (message.length() > 0)) {
            createQRCode(message);
        } else {
            txtMessage.setError("Inserisci un messaggio da codificare!");
        }
    }

    private void createQRCode(String message) {

        final int qrDimension = 800;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            final BitMatrix bitMatrix = qrCodeWriter.encode(message, BarcodeFormat.QR_CODE, qrDimension, qrDimension);
            final int matrixHeight = bitMatrix.getHeight();
            final int matrixWidth = bitMatrix.getWidth();
            int[] pixels = new int[matrixWidth * matrixHeight];

            for (int y = 0; y < matrixHeight; y++) {
                final int offset = y * matrixWidth;
                for (int x = 0; x < matrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmapQRCode = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            bitmapQRCode.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
            imgQR.setImageBitmap(bitmapQRCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }


    }
}
