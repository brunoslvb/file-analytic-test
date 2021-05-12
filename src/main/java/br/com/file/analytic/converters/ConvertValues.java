package br.com.file.analytic.converters;

import br.com.file.analytic.models.Customer;
import br.com.file.analytic.models.Item;
import br.com.file.analytic.models.Sale;
import br.com.file.analytic.models.Salesperson;

import java.util.ArrayList;
import java.util.List;

public class ConvertValues {

    public static Salesperson convertValuesToSalesperson(String[] values) {
        Salesperson salesperson = new Salesperson();

        salesperson.setId(values[0]);
        salesperson.setCpf(values[1]);
        salesperson.setName(values[2]);
        salesperson.setSalary(Double.parseDouble(values[3]));

        return salesperson;
    }

    public static Customer convertValuesToCustomer(String[] values) {
        Customer customer = new Customer();

        customer.setId(values[0]);
        customer.setCnpj(values[1]);
        customer.setName(values[2]);
        customer.setBusiness(values[3]);

        return customer;
    }

    public static Sale convertValuesToSale(String[] values) {
        Sale sale = new Sale();

        String[] saleItens = values[2].replaceAll("\\[|]", "").split(",");

        List<Item> itens = new ArrayList<Item>();

        Double totalPrice = 0.0;

        for(String saleItem: saleItens) {
            String[] itensValues = saleItem.split("-");

            Item item = new Item();

            item.setId(itensValues[0]);
            item.setQuantity(Integer.parseInt(itensValues[1]));
            item.setPrice(Double.parseDouble(itensValues[2]));

            totalPrice = totalPrice + (Double.parseDouble(itensValues[2]) * Integer.parseInt(itensValues[1]));

            itens.add(item);
        }

        sale.setId(values[0]);
        sale.setSaleId(values[1]);
        sale.setItem(itens);
        sale.setSalesmanName(values[3]);
        sale.setTotalPrice(totalPrice);

        return sale;
    }

}
