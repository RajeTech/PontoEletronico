package com.pm.ramilton.pontoeletronico;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pm.ramilton.pontoeletronico.Dao.ViagemDao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.media.CamcorderProfile.get;


public class fragment_PontoEletronico extends Fragment {

    View view;
    private Button BtnInicioJornada, BtnParadaCliente, BtnParadaAlmoco, BtnRetornoAlmoco, BtnParadaPernoite, BtnFimJornada;
    private ViagemDao viagemDao;

   // private TextView txtCoordenadas;
    private TextView TvLembrete, TvContadorParadaCliente, TvDataInicioJornada;
    private Chronometer cronometro;
    boolean rodando = false;
    double lat;
    double lon;
    String co, da, hora, fkonjectiduser;
    int clique = 0, contador = 0, cliquenovamente = 0;
    String MensagemJustificativa;
    boolean isGPSEnable;
    LocationManager lm;

    public fragment_PontoEletronico() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pontoeletronico_fragment, container, false);

        BtnInicioJornada = (Button) view.findViewById(R.id.btnInicioJornada);
        BtnParadaCliente = (Button) view.findViewById(R.id.btnParadaCliente);
        BtnParadaAlmoco = (Button) view.findViewById(R.id.btnParadaAlmoco);
        BtnRetornoAlmoco = (Button) view.findViewById(R.id.btnRetornoAlmoco);
        BtnParadaPernoite = (Button) view.findViewById(R.id.btnParadaPernoite);
        BtnFimJornada = (Button) view.findViewById(R.id.btnFimJornada);
        cronometro = (Chronometer) view.findViewById(R.id.txtCordenada);
        TvLembrete = (TextView) view.findViewById(R.id.txtlembrete);
        TvContadorParadaCliente = (TextView) view.findViewById(R.id.tvContadorParadaCliente);
        TvDataInicioJornada = (TextView) view.findViewById(R.id.tvdataInicioJornada);

        cronometro.setVisibility(View.INVISIBLE);


        PorterDuffColorFilter corbotaoiniciar = new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
        BtnInicioJornada.getBackground().setColorFilter(corbotaoiniciar);

        viagemDao = new ViagemDao(getContext());

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);

        BtnInicioJornada.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                MensagemJustificativa = "";

                lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (cliquenovamente == 1) {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(getContext());
                    mensagem.setMessage("Informe o motivo para iniciar a jornada!!!");
                    mensagem.setTitle("Justificativa do novo início da jornada");
                    final EditText Edtjustificativa = new EditText(getContext());
                    mensagem.setView(Edtjustificativa);

                    mensagem.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MensagemJustificativa = Edtjustificativa.getText().toString();
                            if (MensagemJustificativa.length() == 0) {
                                Toast.makeText(getContext(), "Informe o motivo!!!", Toast.LENGTH_LONG).show();
                            }
                            if (!isGPSEnable) {
                                Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                            } else {

                                if (clique == 0) {
                                    clique = 1;

                                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                                    BtnInicioJornada.getBackground().setColorFilter(colorFilter);
                                    BtnInicioJornada.setEnabled(false);

                                    BtnParadaCliente.setVisibility(View.VISIBLE);
                                    TvContadorParadaCliente.setVisibility(View.VISIBLE);
                                    BtnParadaAlmoco.setVisibility(View.VISIBLE);
                                    BtnParadaPernoite.setVisibility(View.VISIBLE);
                                    BtnFimJornada.setVisibility(View.VISIBLE);


                                    BtnParadaCliente.setEnabled(true);
                                    BtnParadaAlmoco.setEnabled(true);
                                    BtnParadaPernoite.setEnabled(true);
                                    BtnFimJornada.setEnabled(true);
                                    BtnRetornoAlmoco.setEnabled(true);


                                    PorterDuffColorFilter corbotaoiniciar = new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
                                    BtnParadaCliente.getBackground().setColorFilter(corbotaoiniciar);
                                    BtnParadaAlmoco.getBackground().setColorFilter(corbotaoiniciar);
                                    BtnParadaPernoite.getBackground().setColorFilter(corbotaoiniciar);
                                    BtnRetornoAlmoco.getBackground().setColorFilter(corbotaoiniciar);
                                    BtnFimJornada.getBackground().setColorFilter(corbotaoiniciar);

                                    GPStracker g = new GPStracker(getContext());

                                    Location l = g.getLocation();

                                    if (l != null) {
                                        lat = l.getLatitude();
                                        lon = l.getLongitude();
                                    }

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                                    Date data = new Date();

                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(data);
                                    Date data_atual = cal.getTime();

                                    String data_completa = dateFormat.format(data_atual);
                                    String hora_atual = dateFormat_hora.format(data_atual);

                                    TvDataInicioJornada.setText("Data da Jornada: " + data_completa);

                                    if (!rodando) {
                                        iniciarCronometro();
                                    } else {
                                        pararCronometro();
                                    }

                                    TvLembrete.setText("Última ação: Início de jornada " + hora_atual);

                                    ParseUser usuarioatual = ParseUser.getCurrentUser();
                                    String codigousuario = usuarioatual.getObjectId();


                                    ParseObject objetoviagem = new ParseObject("Viagem");
                                    objetoviagem.put("fkOnjectIdUser", codigousuario);
                                    objetoviagem.put("Data", data_completa);
                                    objetoviagem.put("Hora", hora_atual);
                                    objetoviagem.put("JustificativaInicioJornada", MensagemJustificativa);
                                    objetoviagem.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Viagem");
                                    query.whereEqualTo("fkOnjectIdUser", codigousuario);
                                    query.orderByDescending("createdAt");
                                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                                        public void done(ParseObject player, ParseException e) {
                                            if (e == null) {
                                                co = player.getObjectId();
                                                da = player.getString("Data");
                                                hora = player.getString("Hora");
                                                fkonjectiduser = player.getString("fkOnjectIdUser");

                                                viagemDao.inserirInformacaoViagem(co, da, hora, fkonjectiduser, MensagemJustificativa);

                                            } else {
                                                Log.i("dados", "erro " + e);
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Jornada já iniciada!!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });

                    mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    mensagem.setCancelable(false);
                    mensagem.show();


                } else {

                    LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (!isGPSEnable) {
                        Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                    } else {

                        if (clique == 0) {
                            clique = 1;

                            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                            BtnInicioJornada.getBackground().setColorFilter(colorFilter);
                            BtnInicioJornada.setEnabled(false);

                            BtnParadaCliente.setVisibility(View.VISIBLE);
                            TvContadorParadaCliente.setVisibility(View.VISIBLE);
                            BtnParadaAlmoco.setVisibility(View.VISIBLE);
                            BtnParadaPernoite.setVisibility(View.VISIBLE);
                            BtnFimJornada.setVisibility(View.VISIBLE);


                            BtnParadaCliente.setEnabled(true);
                            BtnParadaAlmoco.setEnabled(true);
                            BtnParadaPernoite.setEnabled(true);
                            BtnFimJornada.setEnabled(true);
                            BtnRetornoAlmoco.setEnabled(true);


                            PorterDuffColorFilter corbotaoiniciar = new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
                            BtnParadaCliente.getBackground().setColorFilter(corbotaoiniciar);
                            BtnParadaAlmoco.getBackground().setColorFilter(corbotaoiniciar);
                            BtnParadaPernoite.getBackground().setColorFilter(corbotaoiniciar);
                            BtnRetornoAlmoco.getBackground().setColorFilter(corbotaoiniciar);
                            BtnFimJornada.getBackground().setColorFilter(corbotaoiniciar);

                            GPStracker g = new GPStracker(getContext());

                            Location l = g.getLocation();

                            if (l != null) {
                                lat = l.getLatitude();
                                lon = l.getLongitude();
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                            Date data = new Date();

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(data);
                            Date data_atual = cal.getTime();

                            String data_completa = dateFormat.format(data_atual);
                            String hora_atual = dateFormat_hora.format(data_atual);

                            TvDataInicioJornada.setText("Data da Jornada: " + data_completa);

                            if (!rodando) {
                                iniciarCronometro();
                            } else {
                                pararCronometro();
                            }

                            TvLembrete.setText("Última ação: Início de jornada " + hora_atual);

                            ParseUser usuarioatual = ParseUser.getCurrentUser();
                            String codigousuario = usuarioatual.getObjectId();


                            ParseObject objetoviagem = new ParseObject("Viagem");
                            objetoviagem.put("fkOnjectIdUser", codigousuario);
                            objetoviagem.put("Data", data_completa);
                            objetoviagem.put("Hora", hora_atual);
                            objetoviagem.put("JustificativaInicioJornada", MensagemJustificativa);
                            objetoviagem.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Viagem");
                            query.whereEqualTo("fkOnjectIdUser", codigousuario);
                            query.orderByDescending("createdAt");
                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject player, ParseException e) {
                                    if (e == null) {
                                        co = player.getObjectId();
                                        da = player.getString("Data");
                                        hora = player.getString("Hora");
                                        fkonjectiduser = player.getString("fkOnjectIdUser");

                                        viagemDao.inserirInformacaoViagem(co, da, hora, fkonjectiduser, MensagemJustificativa);

                                    } else {
                                        Log.i("dados", "erro " + e);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Jornada já iniciada!!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });

        BtnParadaCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                }else {

                    contador++;
                    TvContadorParadaCliente.setText(""+contador);

                    GPStracker g = new GPStracker(getContext());
                    Location l = g.getLocation();

                    if (l != null) {
                        lat = l.getLatitude();
                        lon = l.getLongitude();
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String data_completa = dateFormat.format(data_atual);
                    String hora_atual = dateFormat_hora.format(data_atual);

                    long tempo;

                    if (!rodando) {
                        iniciarCronometro();
                    } else {
                        pararCronometro();
                    }

                    TvLembrete.setText("Última ação: Parada em Cliente " + hora_atual);
                    String descricao = "Parada em Cliente";

                    ParseObject objetoparadas = new ParseObject("Paradas");
                    objetoparadas.put("fkObjectIdViagem", viagemDao.retornarCodigo());
                    objetoparadas.put("Descricao", descricao);
                    objetoparadas.put("Data", data_completa);
                    objetoparadas.put("Latitude",lat);
                    objetoparadas.put("Longitude",lon);
                    objetoparadas.put("Hora", hora_atual);
                    objetoparadas.put("Cronometro", cronometro.getText().toString());
                    objetoparadas.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

           }
        });

        BtnParadaAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                } else {

                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    BtnParadaAlmoco.getBackground().setColorFilter(colorFilter);
                    BtnParadaAlmoco.setEnabled(false);

                    BtnRetornoAlmoco.setVisibility(View.VISIBLE);
                    BtnParadaCliente.setEnabled(false);
                    BtnParadaCliente.getBackground().setColorFilter(colorFilter);
                    BtnParadaPernoite.setEnabled(false);
                    BtnParadaPernoite.getBackground().setColorFilter(colorFilter);
                    GPStracker g = new GPStracker(getContext());
                    Location l = g.getLocation();

                    if (l != null) {
                        lat = l.getLatitude();
                        lon = l.getLongitude();
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String data_completa = dateFormat.format(data_atual);
                    String hora_atual = dateFormat_hora.format(data_atual);

                    long tempo;

                    if (!rodando) {
                        iniciarCronometro();
                    } else {
                        pararCronometro();
                    }

                    TvLembrete.setText("Última ação: Parada para Almoço " + hora_atual);

                    String descricao = "Parada para Almoço";

                    ParseObject objetoparadas = new ParseObject("Paradas");
                    objetoparadas.put("fkObjectIdViagem", viagemDao.retornarCodigo());
                    objetoparadas.put("Descricao", descricao);
                    objetoparadas.put("Data", data_completa);
                    objetoparadas.put("Latitude",lat);
                    objetoparadas.put("Longitude",lon);
                    objetoparadas.put("Hora", hora_atual);
                    objetoparadas.put("Cronometro", cronometro.getText().toString());
                    objetoparadas.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }



            }
        });

        BtnRetornoAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                } else {

                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    BtnRetornoAlmoco.getBackground().setColorFilter(colorFilter);
                    BtnRetornoAlmoco.setEnabled(false);

                    BtnRetornoAlmoco.setVisibility(View.VISIBLE);
                    BtnParadaCliente.setEnabled(true);
                    BtnParadaPernoite.setEnabled(true);
                    GPStracker g = new GPStracker(getContext());
                    Location l = g.getLocation();

                    if (l != null) {
                        lat = l.getLatitude();
                        lon = l.getLongitude();
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String data_completa = dateFormat.format(data_atual);
                    String hora_atual = dateFormat_hora.format(data_atual);

                    long tempo;

                    if (!rodando) {
                        iniciarCronometro();
                    } else {
                        pararCronometro();
                    }

                    TvLembrete.setText("Última ação: Retorno de Almoço " + hora_atual);

                    String descricao = "Retorno de Almoço";

                    ParseObject objetoparadas = new ParseObject("Paradas");
                    objetoparadas.put("fkObjectIdViagem", viagemDao.retornarCodigo());
                    objetoparadas.put("Descricao", descricao);
                    objetoparadas.put("Data", data_completa);
                    objetoparadas.put("Latitude",lat);
                    objetoparadas.put("Longitude",lon);
                    objetoparadas.put("Hora", hora_atual);
                    objetoparadas.put("Cronometro", cronometro.getText().toString());
                    objetoparadas.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        BtnParadaPernoite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                } else {

                    PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    BtnRetornoAlmoco.getBackground().setColorFilter(colorFilter);
                    BtnInicioJornada.getBackground().setColorFilter(colorFilter);
                    BtnParadaCliente.getBackground().setColorFilter(colorFilter);
                    BtnParadaAlmoco.getBackground().setColorFilter(colorFilter);
                    BtnParadaPernoite.getBackground().setColorFilter(colorFilter);

                    BtnRetornoAlmoco.setEnabled(false);
                    BtnParadaPernoite.setEnabled(false);
                    BtnParadaAlmoco.setEnabled(false);
                    BtnParadaCliente.setEnabled(false);
                    BtnInicioJornada.setEnabled(false);

                    GPStracker g = new GPStracker(getContext());
                    Location l = g.getLocation();

                    if (l != null) {
                        lat = l.getLatitude();
                        lon = l.getLongitude();
                    }

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String data_completa = dateFormat.format(data_atual);
                    String hora_atual = dateFormat_hora.format(data_atual);

                    Log.i("data_completa", data_completa);
                    Log.i("hora_atual", hora_atual);

                    long tempo;

                    if (!rodando) {
                        iniciarCronometro();

                    } else {
                        pararCronometro();

                    }

                    TvLembrete.setText("Última ação: Parada de Pernoite " + hora_atual);

                    String descricao = "Parada de Pernoite";

                    ParseObject objetoparadas = new ParseObject("Paradas");
                    objetoparadas.put("fkObjectIdViagem", viagemDao.retornarCodigo());
                    objetoparadas.put("Descricao", descricao);
                    objetoparadas.put("Data", data_completa);
                    objetoparadas.put("Latitude",lat);
                    objetoparadas.put("Longitude",lon);
                    objetoparadas.put("Hora", hora_atual);
                    objetoparadas.put("Cronometro", cronometro.getText().toString());
                    objetoparadas.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        BtnFimJornada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (clique == 1) {
                    clique = 0;

                    cliquenovamente = 1;

                    LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    boolean isGPSEnable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (!isGPSEnable) {
                        Toast.makeText(getContext(), "É necessário habilitar primeiro o GPS!!!", Toast.LENGTH_LONG).show();
                    } else {

                        TvContadorParadaCliente.setText("0");

                        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        BtnFimJornada.getBackground().setColorFilter(colorFilter);
                        BtnFimJornada.setEnabled(false);
                        BtnInicioJornada.setEnabled(true);
                        BtnFimJornada.setVisibility(View.INVISIBLE);
                        BtnParadaCliente.setVisibility(View.INVISIBLE);
                        BtnParadaAlmoco.setVisibility(View.INVISIBLE);
                        BtnRetornoAlmoco.setVisibility(View.INVISIBLE);
                        BtnParadaPernoite.setVisibility(View.INVISIBLE);
                        TvContadorParadaCliente.setVisibility(View.INVISIBLE);

                        PorterDuffColorFilter corbotaoiniciar = new PorterDuffColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
                        BtnInicioJornada.getBackground().setColorFilter(corbotaoiniciar);

                        GPStracker g = new GPStracker(getContext());
                        Location l = g.getLocation();

                        if (l != null) {
                            lat = l.getLatitude();
                            lon = l.getLongitude();
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                        Date data = new Date();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(data);
                        Date data_atual = cal.getTime();

                        String data_completa = dateFormat.format(data_atual);
                        String hora_atual = dateFormat_hora.format(data_atual);

                        long tempo;

                        if (!rodando) {
                            iniciarCronometro();
                        } else {
                            pararCronometro();
                        }

                        TvLembrete.setText("Última ação: Fim de jornada " + hora_atual);

                        String descricao = "Fim de Jornada";

                        ParseObject objetoparadas = new ParseObject("Paradas");
                        objetoparadas.put("fkObjectIdViagem", viagemDao.retornarCodigo());
                        objetoparadas.put("Descricao", descricao);
                        objetoparadas.put("Data", data_completa);
                        objetoparadas.put("Latitude", lat);
                        objetoparadas.put("Longitude", lon);
                        objetoparadas.put("Hora", hora_atual);
                        objetoparadas.put("Cronometro", cronometro.getText().toString());
                        objetoparadas.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getContext(), "Dados gravados com sucesso!!!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Erro ao gravar os dados!!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Fim de Jornada!!!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;

    }

    public void iniciarCronometro(){
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();
        rodando = true;
    }

    public void pararCronometro(){
        cronometro.stop();
        rodando = false;
    }

}
