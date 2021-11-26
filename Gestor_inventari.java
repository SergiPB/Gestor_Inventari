import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Gestor_inventari {

    static Scanner teclat = new Scanner(System.in);
    static Connection connexioBD;

    public static void main(String[] args) throws IOException {

        try {
            connexioBD();
            menu();
            desconexio();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    static void connexioBD() throws SQLException {
        String servidor = "jdbc:mysql://localhost:3306/";
        String bbdd = "empresa";
        String user = "root";
        String password = "1234";

        connexioBD = DriverManager.getConnection(servidor + bbdd, user, password);

    }

    static void menu() throws SQLException, IOException {
        boolean sortir = false;
        do {

            System.out.println("******* MENU GESTOR INVENTARI *******");
            System.out.println("1. Gestio Productes");
            System.out.println("2. Actualitzar stock");
            System.out.println("3. Preparar comanda");
            System.out.println("4. Analitzar les comandes");
            System.out.println("5. Sortir");
            System.out.println("TRIA UNA OPCIÓ");

            int opcio = teclat.nextInt();

            switch (opcio) {
            case 1:
                gestorProductes();
                break;
            case 2:
                actualitzarStok();
                break;
            case 3:
                prepararComanda();
                break;
            case 4:
                analitzarComandes();
                break;
            case 5:
                sortir = true;
                break;
            default:
                System.out.println("Opció no valida");

            }
        } while (!sortir);
    }

    static void gestorProductes() throws SQLException {

        System.out.println("******* GESTÓ PRODUCTES *******");
        System.out.println("1. LLISTA tots els productes");
        System.out.println("2. CONSULTA un producte");
        System.out.println("3. ALTA productes");
        System.out.println("4. MODIFICACIO producte");
        System.out.println("5. ESBORRA producte");
        System.out.println("TRIA UNA OPCIÓ");

        int opcio = teclat.nextInt();
        teclat.nextLine();

        switch (opcio) {
        case 1:
            llistarTotsProductes();
            break;
        case 2:
            consutlaProducte();
            break;
        case 3:
            altaProducte();
            break;
        case 4:
            modificacioProducete();
            break;
        case 5:
            baixaProducte();
            break;
        default:
            System.out.println("Opció no valida");

        }

    }

    static void llistarTotsProductes() throws SQLException {
        System.out.println("LLISTATS DE TOTS ELS PRODUCTES");

        String consulta = "select * from productes order by codi;";

        PreparedStatement ps = connexioBD.prepareStatement(consulta);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.print("codi: " + rs.getInt("codi") + "  ");
            System.out.print("nom: " + rs.getString("nom") + "  ");
            System.out.print("material: " + rs.getString("material") + "  ");
            System.out.print("estoc: " + rs.getInt("estoc") + "  ");
            System.out.println("codi_cat: " + rs.getInt("codi_cat"));
        }
    }

    static void consutlaProducte() throws SQLException {
        System.out.println("CONSUTA UN PRODUCTE");
        System.out.println("Codi del producte que vols consulta:");
        int codi = teclat.nextInt();

        String consulta = "select * from productes where codi=?;";

        PreparedStatement ps = connexioBD.prepareStatement(consulta);

        ps.setInt(1, codi);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.print("codi: " + rs.getInt("codi") + "  ");
            System.out.print("nom: " + rs.getString("nom") + "  ");
            System.out.print("material: " + rs.getString("material") + "  ");
            System.out.print("estoc: " + rs.getInt("estoc") + "  ");
            System.out.println("codi_cat: " + rs.getInt("codi_cat"));
        }
    }

    static void altaProducte() throws SQLException {
        System.out.println("ALTA PRODUCTE");
        System.out.println("Nom:");
        String nom = teclat.nextLine();

        System.out.println("Material:");
        String mat = teclat.nextLine();

        System.out.println("Estoc:");
        int estoc = teclat.nextInt();

        System.out.println("Categoria:");
        int codi_cat = teclat.nextInt();

        String insercio = "insert into productes (nom, material, estoc, codi_cat) value (?,?,?,?);";
        PreparedStatement sentencia = connexioBD.prepareStatement(insercio);

        sentencia.setString(1, nom);
        sentencia.setString(2, mat);
        sentencia.setInt(3, estoc);
        sentencia.setInt(4, codi_cat);

        sentencia.executeUpdate();
    }

    static void modificacioProducete() throws SQLException {
        String nom = "";
        String mat = "";
        int estoc = 0;
        int codi_cat = 0;

        System.out.println("MODIFICA PRODUCTE");

        System.out.println("Codi del producte que vols modificar:");
        int codi = teclat.nextInt();
        teclat.nextLine();

        String consulta = "select * from productes where codi=?;";
        PreparedStatement ps = connexioBD.prepareStatement(consulta);
        ps.setInt(1, codi);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            nom = rs.getString("nom");
            mat = rs.getString("material");
            estoc = rs.getInt("estoc");
            codi_cat = rs.getInt("codi_cat");
        }

        System.out.println("Vols modificar el nom? (si/no)");
        String resp = teclat.nextLine();

        if (resp.equalsIgnoreCase("si")) {
            System.out.println("Nom:");
            nom = teclat.nextLine();
        }

        System.out.println("Vols modificar el material? (si/no)");
        resp = teclat.nextLine();

        if (resp.equalsIgnoreCase("si")) {
            System.out.println("Material:");
            mat = teclat.nextLine();
        }

        System.out.println("Vols modificar l'estoc? (si/no)");
        resp = teclat.nextLine();

        if (resp.equalsIgnoreCase("si")) {
            System.out.println("Estoc:");
            estoc = teclat.nextInt();
            teclat.nextLine();
        }

        System.out.println("Vols modificar la categortia? (si/no)");
        resp = teclat.nextLine();

        if (resp.equalsIgnoreCase("si")) {
            System.out.println("Categoria:");
            codi_cat = teclat.nextInt();
        }

        String modifica = "UPDATE productes SET nom=?, material=?, estoc=?, codi_cat=? WHERE codi=?;";
        PreparedStatement sentencia = connexioBD.prepareStatement(modifica);

        sentencia.setString(1, nom);
        sentencia.setString(2, mat);
        sentencia.setInt(3, estoc);
        sentencia.setInt(4, codi_cat);
        sentencia.setInt(5, codi);

        sentencia.executeUpdate();

        consulta = "select * from productes where codi=?;";
        ps = connexioBD.prepareStatement(consulta);
        ps.setInt(1, codi);

        rs = ps.executeQuery();

        while (rs.next()) {
            System.out.print("codi: " + rs.getInt("codi") + "  ");
            System.out.print("nom: " + rs.getString("nom") + "  ");
            System.out.print("material: " + rs.getString("material") + "  ");
            System.out.print("estoc: " + rs.getInt("estoc") + "  ");
            System.out.println("codi_cat: " + rs.getInt("codi_cat"));
        }

        teclat.nextLine();
    }

    static void baixaProducte() throws SQLException {
        System.out.println("ESBORRA PRODUCTE");
        System.out.println("Codi del producte que vols esborra:");
        int codi = teclat.nextInt();

        String elimina = "delete from productes where codi=?;";
        PreparedStatement sentencia = connexioBD.prepareStatement(elimina);

        sentencia.setInt(1, codi);

        sentencia.executeUpdate();
    }

    static void actualitzarStok() throws IOException, SQLException {
        File fitxer = new File("files/ENTRADES PENDENTS");
        fitxer.mkdirs();
        File fitxer2 = new File("files/ENTRADES PROCESSADES");
        fitxer2.mkdirs();

        if (fitxer.isDirectory()) {
            File[] fitxers = fitxer.listFiles();

            // Llistar dels fitxers
            for (int i = 0; i < fitxers.length; i++) {
                System.out.println(fitxers[i].getName());
                actualitzarFitxerBD(fitxers[i]);
                moureFitxer(fitxers[i]);
            }

        }

    }

    static void actualitzarFitxerBD(File fitxer) throws IOException, SQLException {
        // Llegeix caràcter a caràcter
        FileReader reader = new FileReader(fitxer);
        // Llegeix linea a linea
        BufferedReader buffer = new BufferedReader(reader);

        String linea;
        while ((linea = buffer.readLine()) != null) {
            System.out.println(linea);

            int posSep = linea.indexOf(":");
            int codiprod = Integer.parseInt(linea.substring(0, posSep));
            int estoc = Integer.parseInt(linea.substring(posSep + 1));

            String modifica = "UPDATE productes SET estoc=estoc+? WHERE codi=?;";
            PreparedStatement sentencia = connexioBD.prepareStatement(modifica);

            sentencia.setInt(1, estoc);
            sentencia.setInt(2, codiprod);

            sentencia.executeUpdate();

        }

        buffer.close();
        reader.close();
    }

    static void moureFitxer(File fitxers) throws IOException {
        FileSystem sistemaFitxers = FileSystems.getDefault();
        Path origen = sistemaFitxers.getPath("files/ENTRADES PENDENTS/" + fitxers.getName());
        Path desti = sistemaFitxers.getPath("files/ENTRADES PROCESSADES/" + fitxers.getName());

        Files.move(origen, desti, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("S'ha mogut a PROCESSATS el fitxer: " + fitxers.getName());

    }

    static void prepararComanda() {

    }

    static void analitzarComandes() {

    }

    static void desconexio() {

    }

}
