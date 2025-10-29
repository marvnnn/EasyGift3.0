package Arquivos;

import Entidades.*;
import aed3.Arquivo;

public class ArquivoListaProduto extends Arquivo<ListaProduto> {

    public ArquivoListaProduto() throws Exception {
        super("listaproduto", ListaProduto.class.getConstructor());
        // Cria o arquivo com cabeçalho se estiver vazio
        if (this.arquivo.length() < 4) {
            this.arquivo.writeInt(0);
        }
    }
}
