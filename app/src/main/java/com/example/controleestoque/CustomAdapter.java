package com.example.controleestoque;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<String> rowid, nomes, valores, quantidadeAtual, quantidadeMinima;
    int position;

//    Instancia o CustomAdapter para receber os valores
    public CustomAdapter(Activity activity, ArrayList rowid, ArrayList nomes, ArrayList valores, ArrayList quantidadeAtual, ArrayList quantidadeMinima){
        this.activity = activity;
        this.rowid = rowid;
        this.nomes = nomes;
        this.valores = valores;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.linha_lista, parent, false);
        return new MyViewHolder(view);
    }

    // Preenche a tela principal com os dados dos produtos
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.position = position;
        holder.txtId.setText(String.valueOf(rowid.get(position)));
        holder.txtNome.setText(String.valueOf(nomes.get(position)));
        holder.txtValor.setText(String.valueOf(valores.get(position)));
        holder.txtQuantidadeAtual.setText(String.valueOf(quantidadeAtual.get(position)));
        holder.txtQuantidadeMin.setText(String.valueOf(quantidadeMinima.get(position)));

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ActivityCadastro.class);
                intent.putExtra("nome", String.valueOf(nomes.get(position)));
                intent.putExtra("valor", String.valueOf(valores.get(position)));
                intent.putExtra("quantidadeAtual", String.valueOf(quantidadeAtual.get(position)));
                intent.putExtra("quantidadeMin", String.valueOf(quantidadeMinima.get(position)));

                activity.startActivityForResult(intent, 2);
            }
        });
    }

//    Contagem dos produtos
    @Override
    public int getItemCount() {
        return nomes.size();
    }

    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(nomes);

            } else {
                for (String produto: nomes) {
                    if (produto.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(produto);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            nomes.clear();
            nomes.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtNome, txtValor, txtQuantidadeAtual, txtQuantidadeMin;
        LinearLayout rowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtQuantidadeAtual = itemView.findViewById(R.id.txtQuantidadeAtual);
            txtQuantidadeMin = itemView.findViewById(R.id.txtQuantidadeMin);

            rowLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
