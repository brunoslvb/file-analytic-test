package br.com.file.analytic;

import br.com.file.analytic.converters.ConvertValues;
import br.com.file.analytic.models.Customer;
import br.com.file.analytic.models.Item;
import br.com.file.analytic.models.Sale;
import br.com.file.analytic.models.Salesperson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadFile {

    private final String delimiter = "ç";

    private List<Salesperson> salespeople = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    public void readCsvFile(String file) throws IOException {

        this.salespeople.clear();
        this.customers.clear();
        this.sales.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(this.delimiter);
                try {
                    switch (values[0]) {
                        case "001":
                            this.salespeople.add(ConvertValues.convertValuesToSalesperson(values));
                            break;
                        case "002":
                            this.customers.add(ConvertValues.convertValuesToCustomer(values));
                            break;
                        case "003":
                            this.sales.add(ConvertValues.convertValuesToSale(values));
                            break;
                        default:
                            break;
                    }
                } catch(Exception err) {
                    System.out.println("Arquivos com conteúdo inválido");
                }
            }
        }
    }

    private String[] outputData() {
        Integer numberOfCustomers = this.customers.size();
        Integer numberOfSalespeople = this.salespeople.size();
        String mostExpensiveSaleId = this.getMostExpensiveSaleId();
        String worstSalesperson = this.getWorstSalesperson();

        String[] data = {
            numberOfCustomers.toString(),
            numberOfSalespeople.toString(),
            mostExpensiveSaleId,
            worstSalesperson
        };

        return data;
    }

    private String getMostExpensiveSaleId(){

        String mostExpensiveSaleId = "Nenhuma venda realizada";

        if(this.sales.size() != 0){

            Double mostExpensiveSale = 0.0;

            for(Sale sale: this.sales) {
                if(sale.getTotalPrice() >= mostExpensiveSale) {
                    mostExpensiveSaleId = sale.getSaleId();
                }
            }
        }

        return mostExpensiveSaleId;
    }

    private String getWorstSalesperson(){

        String worstSalesperson = "Nenhuma venda realizada";

        Double totalPrice = 0.0;

        if(this.sales.size() != 0){

            if(this.sales.size() == 1 && this.customers.size() == 1) return "Existe apenas um vendedor";

            totalPrice = this.sales.get(0).getTotalPrice();

            for(Sale sale: this.sales) {
                if(sale.getTotalPrice() <= totalPrice) {
                    worstSalesperson = sale.getSalesmanName();
                }
            }
        }

        return worstSalesperson;
    }

    public void writeCsvFile(String filename) throws IOException {
        String[] header = {"QtdClientes", "QtdVendedores", "IdVendaMaisCara", "PiorVendedor"};

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            writer.append(String.join(",", header));
            writer.append("\n");

            writer.append(String.join(",", this.outputData()));
            writer.append("\n");

            writer.flush();
        }

    }

    public static void main(String[] args) throws IOException {

        // ReadFile readFile = new ReadFile();

        // readFile.readCsvFile();

        // readFile.writeCsvFile(readFile.outputData());

    }

}
