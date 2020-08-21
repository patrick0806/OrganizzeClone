package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity {
    private TextInputEditText data,categoria,descricao;
    private EditText valor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        valor = findViewById(R.id.editValor);
        data = findViewById(R.id.editData);
        categoria = findViewById(R.id.editCategoria);
        descricao = findViewById(R.id.editDescricao);

        data.setText(DateCustom.dataAtual());
        recuperaDespesaTotal();
    }

    public void salvarDespesa(View view){
        if(validarCamposDespesa()){

            Double despesaGerada = Double.parseDouble(valor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setValor(despesaGerada);
            movimentacao.setData(data.getText().toString());
            String dataEscolhida = data.getText().toString();
            movimentacao.setCategoria(categoria.getText().toString());
            movimentacao.setDescricao(descricao.getText().toString());
            movimentacao.setTipo("d");

            Double despesaAtualizada=despesaTotal+despesaGerada;

            atualizaDespesa(despesaAtualizada);
            movimentacao.salvar(dataEscolhida);
            finish();
        }

    }

    public Boolean validarCamposDespesa(){
        String textoValor = valor.getText().toString();
        String textoData = data.getText().toString();
        String textoCategoria = categoria.getText().toString();
        String textoDescricao = descricao.getText().toString();
        if(!textoValor.isEmpty()){
            if(!textoData.isEmpty()){
                if(!textoCategoria.isEmpty()){
                    if(!textoDescricao.isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(DespesasActivity.this,"Descrição não foi preenchido!",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(DespesasActivity.this,"Categoria não foi preenchido!",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(DespesasActivity.this,"Data não foi preenchido!",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(DespesasActivity.this,"Valor não foi preenchido!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperaDespesaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificaBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal= usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void atualizaDespesa(Double despesa){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificaBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }
}