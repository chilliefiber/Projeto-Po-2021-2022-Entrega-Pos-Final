package ggc.core;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import ggc.core.exception.BadEntryException;
import ggc.core.exception.DuplicatePartnerException;
import ggc.core.exception.UnknownPartnerException;
import ggc.core.exception.UnknownProductException;

class Parser {

    private Warehouse _store;

    Parser(Warehouse w) {
        _store = w;
    }

    void parseFile(String filename) throws IOException, BadEntryException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null)
                parseLine(line);
        }
    }

    private void parseLine(String line) throws BadEntryException {
        String[] components = line.split("\\|");

        switch (components[0]) {
            case "PARTNER":
                parsePartner(components, line);
                break;
            case "BATCH_S":
                parseSimpleProduct(components, line);
                break;
            case "BATCH_M":
                parseAggregateProduct(components, line);
                break;

            default:
                throw new BadEntryException("Invalid type element: " + components[0]);
        }
    }

    //PARTNER|id|nome|endereço
    private void parsePartner(String[] components, String line) throws BadEntryException {
        if (components.length != 4)
            throw new BadEntryException("Invalid partner with wrong number of fields (4): " + line);

        String id = components[1];
        String name = components[2];
        String address = components[3];

        // vindo isto bem formatado nunca ocorrerá a DuplicatePartnerException, se ocorrer RuntimeException
        try {
        	_store.addPartner(id, name, address);
        } catch (DuplicatePartnerException dpe) {
        	throw new RuntimeException();
        }
        
    }

    //BATCH_S|idProduto|idParceiro|prec ̧o|stock-actual
    private void parseSimpleProduct(String[] components, String line) throws BadEntryException {
        if (components.length != 5)
            throw new BadEntryException("Invalid number of fields (4) in simple batch description: " + line);

        String idProduct = components[1];
        String idPartner = components[2];
        double price = Double.parseDouble(components[3]);
        int stock = Integer.parseInt(components[4]);

        
        Product product = null;
        try {
         product = _store.getProduct(idProduct);
        // vou ao Set de produtos buscar a instancia guardada deste produto 
        // e verifico se este novo lote tem o preço mais alto de todos
        } catch (UnknownProductException upe) {
            product = new SimpleProduct(idProduct, price);
            _store.addProduct(product);
        }
        	
        // como os ficheiros vêm bem formatados existe sempre este Parceiro
        // se vier mal formatado chamo 1 runtimeException para crashar o programa
        Partner partner = null;
        try {
        	partner = _store.getPartner(idPartner);
        } catch (UnknownPartnerException upae) {
        	throw new RuntimeException();
        }
        
        Batch newBatch = new Batch(price, stock, product, partner);
        // adicionar este lote à lista de lotes deste produto e parceiro
        product.addBatch(newBatch);
        partner.addBatch(newBatch);
    }

    //BATCH_M|idProduto|idParceiro|prec ̧o|stock-actual|agravamento|componente-1:quantidade-1#...#componente-n:quantidade-n
    private void parseAggregateProduct(String[] components, String line) throws BadEntryException {
        if (components.length != 7)
            throw new BadEntryException("Invalid number of fields (7) in aggregate batch description: " + line);

        String idProduct = components[1];
        String idPartner = components[2];

        // mais uma vez o ficheiro vem bem formatado ha partner de certeza
        Partner partner = null;
        try {
        	partner = _store.getPartner(idPartner);
        } catch (UnknownPartnerException upae) {
        	throw new RuntimeException();
        }
        Product product = null;
        try {
        	product = _store.getProduct(idProduct);
        } catch (UnknownProductException upe) {
            Recipe recipe = new Recipe(Double.parseDouble(components[5]));
            // criar os componentes um a um e inseri los na receita
            for (String componentText : components[6].split("#")) {
                String[] recipeComponent = componentText.split(":");
                Component component = null;
                // os componentes têm de existir caso contrario runtimeexception
                try {
                	component = new Component(_store.getProduct(recipeComponent[0]), Integer.parseInt(recipeComponent[1]));
                } catch (UnknownProductException upre) {
                	throw new RuntimeException();
                }
                recipe.addComponent(component); 
            }
            product = new AggregateProduct(idProduct, recipe); // novo produto agregado
            // adicionar produto à coleção de Produtos do Armazém
            _store.addProduct(product);
        }

        double price = Double.parseDouble(components[3]);
        int stock = Integer.parseInt(components[4]);
        
        // colocar lote criado no produto e parceiro
        Batch newBatch = new Batch(price, stock, product, partner);
        product.addBatch(newBatch);
        partner.addBatch(newBatch);
    }
}

