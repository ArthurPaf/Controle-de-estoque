package venda.p2.controller;

import venda.p2.dao.CategoriaDAO;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaController {
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    public String salvar(Categoria categoria) {
        
        //esse trim n deixa digitar espaço em branco
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            return "Erro: O nome da categoria é obrigatório.";
        }

        
        if (categoriaDAO.salvar(categoria)) {
            return "Categoria '" + categoria.getNome() + "' cadastrada com sucesso!";
        } else {
            return "Erro ao salvar a categoria no banco de dados.";
        }
    }

    
    

    public String excluir(int id) {
        if (categoriaDAO.excluir(id)) {
            return "Categoria excluída com sucesso!";
        } else {
            return "Erro ao excluir: verifique se existem produtos vinculados a esta categoria.";
        }
    }
}