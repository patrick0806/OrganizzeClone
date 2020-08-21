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
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {
    private TextInputEditText data,categoria,descricao;
    private EditText valor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private  Double receitaTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        valor = findViewById(R.id.editValor);
        data = findViewById(R.id.editData);
        categoria = findViewById(R.id.editCategoria);
        descricao = findViewById(R.id.editDescricao);

        data.setText(DateCustom.dataAtual());
        recuperaReceitaTotal();
    }

    public void salvarReceita(View view){
        if(validarCamposReceita()){

            Double receitaGerada = Double.parseDouble(valor.getText().toString());

            movimentacao = new Movimentacao();
            movimentacao.setValor(receitaGerada);
            movimentacao.setData(data.getText().toString());
            String dataEscolhida = data.getText().toString();
            movimentacao.setCategoria(categoria.getText().toString());
            movimentacao.setDescricao(descricao.getText().toString());
            movimentacao.setTipo("r");

            Double receitaAtualizada=receitaTotal+receitaGerada;

            atualizaReceita(receitaAtualizada);
            movimentacao.salvar(dataEscolhida);
            finish();
        }

    }

    public Boolean validarCamposReceita(){
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
                        Toast.makeText(ReceitasActivity.this,"Descrição não foi preenchido!",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else{
                    Toast.makeText(ReceitasActivity.this,"Categoria não foi preenchido!",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(ReceitasActivity.this,"Data não foi preenchido!",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(ReceitasActivity.this,"Valor não foi preenchido!",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void recuperaReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificaBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal= usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void atualizaReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificaBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }
}