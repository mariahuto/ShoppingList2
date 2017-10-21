package com.eet.pma.maria.shoppinglist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {
    private ArrayList<ShoppingItem> item_list; //llista amb tots els elements; després l'utilitzarem juntament amb l'adapter
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = (ListView) findViewById(R.id.llista);
        btn_add = (Button) findViewById(R.id.btn_afegir);
        edit_item = (EditText) findViewById(R.id.edit_item);

        item_list = new ArrayList<>();
        item_list.add(new ShoppingItem("Patates"));
        item_list.add(new ShoppingItem("Sabó"));
        item_list.add(new ShoppingItem("Formatge"));


        adapter = new ShoppingListAdapter(this, R.layout.shopping_item,item_list);

        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem(); //afegir element amb el teclat('done')
                return true;
            }
        });

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                //per saber quan clickem un element
                item_list.get(pos).toggleChecked(); //canvia el valor de checked a no checked, i a l'inversa
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //accio de borrar element
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int pos, long id) {
                maybeRemoveItem(pos);
                return true;
            }
        });
    }


    //afegir element amb el botó de '+'
    public void afegir(View view) {
        addItem();
    }

    private void addItem() {
        String item_text = edit_item.getText().toString();

        if(!item_text.isEmpty()) {     //alternativa = !item_text.equals("")
            item_list.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
        }
        list.smoothScrollToPosition(item_list.size()-1); //perquè la llista es mogui sola fins a l'element que estem afegint
    }

    //preguntem si volem borrar element, si és així ho elimina
    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String fmt = getResources().getString(R.string.missatge);
        builder.setMessage(String.format(fmt,item_list.get(pos).getText()));

        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item_list.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();
    }

}
