package dam.pmdm.parametres;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends Activity {

    Button botoEnviaDades;
    EditText nom;
    RadioGroup radioGroupSexe;
    TextView dadesRebudes;
    Switch switch1;
    RatingBar ratingBar;
    SeekBar seekBar;

    final int SUBACTIVITY_1=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        botoEnviaDades = (Button) findViewById(R.id.b_enviaDades);
        nom = (EditText) findViewById(R.id.et_nom);
        radioGroupSexe = (RadioGroup) findViewById(R.id.rg_sexe);
        dadesRebudes = (TextView) findViewById(R.id.tv_dades_rebudes);
        switch1 = (Switch) findViewById(R.id.switch1);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        //Afegim un Listener al botó
        botoEnviaDades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dadesCorrectes()) {
                    //Cridaré al meu Subactivity
                    Intent i = new Intent(getApplicationContext(), subActivity.class);

                    //Cree un objecte Bundle per a enviar els paràmetres
                    Bundle b = new Bundle();
                    b.putString("Nom", nom.getText().toString());  // Afegim el paràmetre NOM
                    // Afegim el paràmetre SEXE
                    switch (radioGroupSexe.getCheckedRadioButtonId()) {
                        case R.id.rb_mascle:
                            b.putString("Sexe", "Mascle");
                            break;
                        case R.id.rb_femella:
                            b.putString("Sexe", "Femella");
                            break;
                        default:
                            b.putString("Sexe", "Indefinit");
                    }
                    if (switch1.isChecked()) {
                        b.putString("Carnet", "Tiene carnet de conducir");
                    } else {
                        b.putString("Carnet", "No tiene carnet de conducir");
                    }
                    b.putFloat("Valor", ratingBar.getRating());
                    b.putFloat("Valor2", seekBar.getProgress());
                    i.putExtras(b);  //Afegisc l'objecte Bundle a l'Intent
                    startActivityForResult(i, SUBACTIVITY_1); // Cride al subactivity, amb l'Intent (que conté el Bundle)
                } else {
                    Toast.makeText(getApplicationContext(), "Revisa les dades. Ompli-les TOTES", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public boolean dadesCorrectes(){
        if(nom.getText().length()<=0){
            return false;
        }else{
            if(radioGroupSexe.getCheckedRadioButtonId()==-1){
                return false;
            }else{
                return true;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SUBACTIVITY_1:
                gestionaSubActivity1(resultCode,data);
                break;
        }
    }

    public void gestionaSubActivity1(int resultCode, Intent data){

        if(resultCode==RESULT_OK){
            String missatge="";

            Bundle b = data.getExtras();
            int edat = b.getInt("Edat");
            if (edat<18) missatge=new String("Estas fet un xaval!!!");
            if ((edat>=18)&&(edat<25))  missatge=new String("Campeon!!");
            if (edat>=25)  missatge=new String("ai,ai,ai");
            boolean conducir = b.getBoolean("Carnet");
            //Posem
            dadesRebudes.setText(missatge.toString());
            desactivaComponents();
        }else{
            Toast.makeText(getApplicationContext(),"Error en el SubActivity 1",Toast.LENGTH_LONG).show();
        }
    }

    public void desactivaComponents(){
        nom.setEnabled(false);  // desactivem el camp nom
        radioGroupSexe.setEnabled(false);
        //REcorreguem TOTS els radiobutton del radioGroup, per a anar desactivant-los un a un
        for (int i=0;i<radioGroupSexe.getChildCount();i++){
            radioGroupSexe.getChildAt(i).setEnabled(false);
        }
        botoEnviaDades.setEnabled(false); //desactivem el botó
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
