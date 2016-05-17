package pt.ipleiria.simplecontactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<String>();

        contacts.add("André Marques | 961234567");
        contacts.add("Sérgio Silva | 917654321");
        contacts.add("Sofia Pereira | 939876543");
        contacts.add("Nicolas Gonçalves | 244123456");
        contacts.add("Neuza Monteiro | 9633344455");

        // Código para a ListView
        //
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, contacts);
        ListView listView = (ListView) findViewById(R.id.listView_contacts);
        listView.setAdapter(adapter);

        // Código para o Spinner
        //
        Spinner s = (Spinner) findViewById(R.id.spinner_search);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapters);
    }

    public void onClick_search(View view) {
        // ir buscar as referências para a editText, o spinner e a listView
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_contacts);

        // criar uma nova lista, que guarde os contactos pesquisados
        ArrayList<String> searchedContacts = new ArrayList<>();

        // percorrer todos os contactos, e adicionar os que contêm
        // o termo a pesquisar à nova lista
        String termo = et.getText().toString();

        String selectedItem = (String) sp.getSelectedItem();


        if (termo.equals("")) { // a editText está vazia?
            // mostro todos os contactos
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, contacts);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, "Showing all contacts.", Toast.LENGTH_SHORT).show();
        } else { // a editText não está vazia
            if (selectedItem.equals("All")) { // o spinner tem seleccionado o texto All?
                for (String c : contacts) { // para cada c em contacts
                    if (c.contains(termo)) { // se o c contiver o termo
                        searchedContacts.add(c); // adicionar o c à lista searchedContacts
                    }
                }
            } else if (selectedItem.equals("Name")) {
                // pesquisa pelo nome
                for (String c : contacts) {
                    String[] split = c.split("\\|");
                    String name = split[0];
                    name = name.trim();

                    if (name.contains(termo)) {
                        searchedContacts.add(c);
                    }
                }
            } else if (selectedItem.equals("Phone")) {
                // pesquisa pelo telemóvel
                for (String c : contacts) {
                    String[] split = c.split("\\|");
                    String phone = split[1];
                    phone = phone.trim();

                    if (phone.contains(termo)) {
                        searchedContacts.add(c);
                    }
                }
            }
            boolean vazia = searchedContacts.isEmpty();

            if (vazia == false) { // se a lista de resultados não estiver vazia
                // mostrar o conteúdo da lista de contactos pesquisados na listview
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, searchedContacts);
                lv.setAdapter(adapter);

                // mostrar uma mensagem a dizer "Showing searched contacts."
                Toast.makeText(MainActivity.this, "Showing searched contacts.", Toast.LENGTH_SHORT).show();
            } else { // lista de resultados está vazia
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, contacts);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "No results found. Showing all contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick_add(View view) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add, null));

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                // obter referências para as editTexts

                // faz o cast de um diálogo "genérico"
                // para um alertdialog
                AlertDialog al = (AlertDialog) dialog;

                EditText etName =
                        (EditText) al.findViewById(R.id.editText_name);
                EditText etPhone =
                        (EditText) al.findViewById(R.id.editText_phone);

                // obter o nome e telefone das editTexts

                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();

                // criar um novo contacto

                String newContact = name + " | " + phone;

                // adicionar o contacto à lista de contactos

                contacts.add(newContact);

                // dizer à listview para se actualizar

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1, contacts);
                ListView listView = (ListView) findViewById(R.id.listView_contacts);
                listView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Novo contacto adicionado.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, "carreguei no cancel", Toast.LENGTH_SHORT).show();
            }
        });
        // Set other dialog properties

        builder.setTitle("Novo Contacto");

        builder.setMessage("Introduza o nome e o telefone do contacto");

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
