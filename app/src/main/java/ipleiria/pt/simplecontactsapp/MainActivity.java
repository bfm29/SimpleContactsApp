package ipleiria.pt.simplecontactsapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ipleiria.simplecontactsapp.R;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<String>();

        contacts.add("André Marques | 969874563");
        contacts.add("Sérgio (noob) Silva | 918689876");
        contacts.add("Sofia Pereira | 968643553");
        contacts.add("Nicolas Gonçalves | 91785024");
        contacts.add("Neuza Monteiro | 92435691");

        //codigo para a listview
        //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contacts);

        ListView listView = (ListView) findViewById(R.id.listView_contacts);
        listView.setAdapter(adapter);

        //codigo para o spinner
        Spinner s = (Spinner) findViewById(R.id.spinner_search);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_s = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        listView.setAdapter(adapter);

     

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "clicou no item" + position, Toast.LENGTH_SHORT).show();

                contacts.remove(position);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, contacts);

                ListView listView = (ListView) findViewById(R.id.listView_contacts);
                listView.setAdapter(adapter);
            }
        });



    }
    public void onClick_add(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add, null));
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                // obter referencias paras as editText
                //faz o cast de um diálogo "genérico"
                //para um allertdialog

                AlertDialog al = (AlertDialog) dialog;

                EditText etName =
                        (EditText) al.findViewById(R.id.editText_name);
                EditText etPhone =
                        (EditText) al.findViewById(R.id.editText_phone);

                //obter o nome e telefone das editTexts

                String name= etName.getText().toString();
                String phone = etPhone.getText().toString();

                //criar novo contacto
                String newContact = name + " | " + phone;

                // adicionar o contacto a lista de contactos
                contacts.add(newContact);

                // dizer à listview para se atualizar
                ListView lv = (ListView) findViewById(R.id.listView_contacts);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, contacts);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Novo contacto adicionado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, "Carreguei no cancel", Toast.LENGTH_SHORT).show();
            }
        });
// Set other dialog properties
        // ...

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onClick_search(View view) {
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_contacts);

        //criar uma nova lista, que guarde os contactos pesquisados
        ArrayList<String> searchedContacts = new ArrayList<>();

        //percorrer todos os contactos

        String termo = et.getText().toString();

        String selectedItem = (String) sp.getSelectedItem();

        if(termo.equals("")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, contacts);
            lv.setAdapter(adapter);

        }

        if(selectedItem.equals("ALL")) {

            for (String c : contacts) { // para cada c em contactos
                if (c.contains(termo)) { //se o c tiver o termo
                    searchedContacts.add(c); //adiciona o c à lista searchedContacts
                }
            }
        }else if (selectedItem.equals("Name")){
            for (String c : contacts) {
                String[] split = c.split("\\|");
                String name = split[0];

                if (name.contains(termo)){
                    searchedContacts.add(c);
                }
            }
        }else if (selectedItem.equals("Phone")){
            for(String c: contacts){
                String[] split = c.split("\\|");
                String phone = split[1];
                phone = phone.trim();

                if(phone.contains(termo))
                {
                    searchedContacts.add(c);

                }
            }

        }
        boolean vazia = searchedContacts.isEmpty();

        if(vazia == false) {


            //mostrar o conteudo da lista de contactos
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchedContacts);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, "Showing searched contacts", Toast.LENGTH_SHORT).show();
            //mostrar uma mensagem a dizer "Showing searched contacts
        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, contacts);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, "No results found. Showing all contacts", Toast.LENGTH_SHORT).show();
        }


    }
}