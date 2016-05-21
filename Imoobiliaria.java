package poo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Imoobiliaria implements Serializable {

    private HashMap<String, Utilizador> utilizadores;
    private ArrayList<Imovel> imoveis;
    private Utilizador user;
    private ArrayList<Imovel> imfavoritos;
    private Set<Consulta> consultas;
    

    public HashMap<String, Utilizador> getUtilizadores() {
        HashMap<String, Utilizador> user = new HashMap<>();
        for (Utilizador u : this.utilizadores.values()) {
            user.put(u.getEmail(), u.Clone());
        }
        return user;
    }

    public void setUtilizadores(HashMap<String, Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public ArrayList<Imovel> getImoveis() {
        ArrayList<Imovel> res = new ArrayList<>();
        for (Imovel a : this.imoveis) {
            res.add(a.Clone());
        }
        return res;
    }
    
     public ArrayList<Imovel> getImfavoritos() {
        ArrayList<Imovel> res = new ArrayList<>();
        for (Imovel a : this.imfavoritos) {
            res.add(a.Clone());
        }
        return res;
    }

    public void setImfavoritos(ArrayList<Imovel> imfavoritos) {
        this.imfavoritos = imfavoritos;
    }
     

    public String getImoveis2() {
        String res = new String();
        for (Imovel a : this.imoveis) {
            res = a.getIdImovel();
        }
        return res;
    }

    public void setImoveis(ArrayList<Imovel> imoveis) {
        this.imoveis = imoveis;
    }

    public Utilizador getUser() {
        return user;
    }

    public void setUser(Utilizador user) {
        this.user = user;
    }

    public Imoobiliaria(HashMap<String, Utilizador> utilizadores, ArrayList<Imovel> imoveis, Utilizador user, ArrayList<Imovel> imfavoritos) {
        this.utilizadores = utilizadores;
        this.imoveis = imoveis;
        this.user = user;
        this.imfavoritos=imfavoritos;

    }

    public Imoobiliaria(Imoobiliaria m) {
        this.utilizadores = m.getUtilizadores();
        this.imoveis = m.getImoveis();
        this.user = m.getUser();
        this.imfavoritos=m.getImfavoritos();

    }

    public Imoobiliaria() {
        this.utilizadores = new HashMap<>();
        this.imoveis = new ArrayList<>();
        this.user = new Utilizador() {
        };
        this.imfavoritos=new ArrayList<>();

    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Utilizador: " + this.getUtilizadores() + "\n");
        s.append("Imóvel: " + this.getImoveis() + "\n");
        s.append("Utilizador: " + this.getUser() + "\n");

        return s.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Imoobiliaria other = (Imoobiliaria) obj;
        if (!Objects.equals(this.utilizadores, other.utilizadores)) {
            return false;
        }
        if (!Objects.equals(this.imoveis, other.imoveis)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }

    public Imoobiliaria Clone() {
        return new Imoobiliaria(this);
    }

    public void gravaObj(String fich) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fich));
        oos.writeObject(this);

        oos.flush();
        oos.close();
    }

    public static Imoobiliaria leObj(String fich) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fich));

        Imoobiliaria te = (Imoobiliaria) ois.readObject();

        ois.close();
        return te;
    }

    public void log(String f, boolean ap) throws IOException {
        FileWriter fw = new FileWriter(f, ap);
        fw.write("\n----------- LOG - LOG - LOG - LOG - LOG ----------------\n");
        fw.write(this.toString());
        fw.write("\n----------- LOG - LOG - LOG - LOG - LOG ----------------\n");
        fw.flush();
        fw.close();
    }

    public static Imoobiliaria initApp() {
        Imoobiliaria imo;
        try {
            imo = Imoobiliaria.leObj("estado.tabemp");
        } catch (IOException e) {
            imo = new Imoobiliaria();
            System.out.println("Não existem dados gravados|\nErro de leitura.");
        } catch (ClassNotFoundException e) {
            imo = new Imoobiliaria();
            System.out.println("Não é possível ler os dados!\nFicheiro com formato desconhecido.");
        } catch (ClassCastException e) {
            imo = new Imoobiliaria();
            System.out.println("Não é possível ler os dados!\nErro de formato.");
        }
        return imo;
    }

    public void fechaSessao() {
        String fich = new String();
        try {
            gravaObj(fich);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.exit(0);
    }

    public boolean registo(String email) {
        return (this.utilizadores.containsKey(email));
    }

    public void registarUtilizador(Utilizador utilizador) throws UtilizadorExistenteException {
        if (this.utilizadores.containsKey(utilizador.getEmail())) {
            throw new UtilizadorExistenteException("Este email já existe");
        } else {
            this.utilizadores.put(utilizador.getEmail(), utilizador.Clone());
        }
    }

   

    public boolean login(String email, String password) {
        return (this.utilizadores.containsKey(email) && this.utilizadores.get(email).getPassword().equals(password));
    }

    public void iniciaSessao(String email, String password) throws SemAutorizacaoException {
        if (this.utilizadores.containsKey(email) && this.utilizadores.get(email).getPassword().equals(password)) {
            System.out.println("Login com sucesso");
        } else {
            throw new SemAutorizacaoException("Dados incorrectos");
        }
    }

    public void registaImovel(Imovel im) throws ImovelExisteException, SemAutorizacaoException {
        if (checkVendedor()) {
            throw new SemAutorizacaoException("Não tem autorização para adicionar imóveis!");
        } else if (this.imoveis.contains(im)) {
            throw new ImovelExisteException("Este imóvel já existe!");
        }
        this.imoveis.add(im.Clone());
    }

    public boolean elmImovel(Imovel nome) {
        return this.imoveis.remove(nome);
    }

    public void setEstado(String idImovel, String estado) throws ImovelInexistenteException, SemAutorizacaoException, EstadoInvalidoException {
        if (!this.imoveis.contains(idImovel)) {
            throw new ImovelInexistenteException("O Id de Imóvel não existe!");
        } else if (estado != "Vendido" && estado != "Em venda" && estado != "Reservado") {
            throw new EstadoInvalidoException("Estado não é válido!");
        }
        for (Imovel i : imoveis) {
            if (i.getIdImovel().equals(idImovel)) {
                System.out.println("cena");
                new Imovel(i) {
                }.setEstado(estado);
            }
        }
    }

    public List<Imovel> getImovel(String classe, int preco) {
        
        TreeSet<Imovel> setImovel = new TreeSet<Imovel>(new ComparatorPreco());
        for (Imovel i : imoveis) {
            if(i.getPrecoP() <= preco && i.getTipo().equals(classe)){
                setImovel.add(i.Clone());
            }
        }
        return new ArrayList<>(setImovel);
    }

    public List<Habitavel> getHabitaveis(int preco) {
        List<Habitavel> hab = new ArrayList<Habitavel>();

        for (Imovel i : imoveis) {
            while (i.getPrecoP() <= preco) {

                hab.add((Habitavel) i);

            }
        }
        return hab;
    }

   /* public Map<Imovel, Vendedor> getMapeamentoImoveis() {
        Map<Imovel, Vendedor> mapImo = new HashMap<Imovel, Vendedor>();
        Vendedor v = new Vendedor();
        for (Imovel i : imoveis) {
            for (Utilizador u : utilizadores.values()) {
                if (i.getIdP() == v.getId()) { // não funciona
                    v = (Vendedor) u.Clone();
                }
                mapImo.put(i, v);
            }
        }
        return mapImo;
    }*/

    public boolean tipoImovel(String tipo, Imovel i) {
        switch (tipo) {
            case "Terreno":
                if (i instanceof Terreno);
                break;
            case "Moradia":
                if (i instanceof Moradia);
                break;
            case "Apartamento":
                if (i instanceof Apartamento);
                break;
            case "Loja":
                if (i instanceof Loja);
                break;
        }
        return false;
    }

    public String vertipoImovel(String s, Imovel i) throws TipoInexistenteException {
        if (tipoImovel(s, i)) {
            return s;
        } else {
            throw new TipoInexistenteException("Tipo de imóvel não existe");
        }
    }

    public void setFavorito(String idImovel) throws ImovelInexistenteException, SemAutorizacaoException{
     if (checkComprador()) {
            if (imoveis.contains(idImovel)) {
                Comprador c = (Comprador) user;
                c.addFavorito(idImovel);
            } else {
                throw new ImovelInexistenteException("Este imóvel não existe");
            }
        } else {
            throw new SemAutorizacaoException("Falta de privilégios");
        }
    }

    public boolean checkComprador(){
       return (user instanceof Comprador);
            
    }
    
    public boolean checkVendedor(){
        return (user instanceof Vendedor);
    }
   
    /* public TreeSet<Imovel> getFavoritos(){
        TreeSet<Imovel> res=new TreeSet<>();
        if (checkComprador()) {     
           TreeSet<Imovel> fav = new TreeSet<Imovel>(new ComparatorPreco());
           Comprador c=(Comprador) user;
           ArrayList<String> favoritos=c.getImfavoritos();
            for(Imovel i: imoveis){
            if(favoritos.contains(i.getIdImovel()))
                   fav.add(i.Clone());
            }
             
            Iterator<Imovel> iterator = fav.descendingIterator();   
            for (int i = 0; i <= 10 && iterator.hasNext(); i++ ) {
            res.add(iterator.next()); }
        } 
        return res;
    }*/
   /*public TreeSet<Imovel> getFavoritos() throws SemAutorizacaoException {

        if (checkComprador()) {
            TreeSet<Imovel> novo = new TreeSet<>(new ComparatorPreco());

            Comprador c = (Comprador) user;
            TreeSet<Imovel> imvs = c.getImfavoritos().
                    

            return imvs;

        } else {
            throw new SemAutorizacaoException("Não está autenticado como comprador.");
        }
    }*/
    
public List<Consulta> getConsultas(){

  List<Consulta> res = new ArrayList<Consulta>();
  //ORDENAR TODAS AS CONSULTAS
  TreeSet<Consulta> setConsultas = new TreeSet<Consulta>(new ComparatorData());
  for (Imovel imovel: imoveis) {
    for(Consulta consulta: imovel.getConsulta()){
      setConsultas.add(consulta.clone());
    }    
   }
    
   Iterator<Consulta> iterator = setConsultas.descendingIterator();   
   for (int i = 0; i <= 10 && iterator.hasNext(); i++ ) {
      res.add(iterator.next());           
   }

   return res;
}

public void adicionaConsulta(Consulta consulta, Imovel imovel){
        for(Imovel i : imoveis){
            if(imovel.equals(i)){
                List<Consulta> consultas = i.getConsulta();
                consultas.add(consulta.clone());
                i.setConsultas(consultas);
            }
        }
    }
}



       
        