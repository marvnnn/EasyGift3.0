package Arquivos;

import Entidades.*;
import aed3.Arquivo;

public class ArquivoProduto extends Arquivo<Produto> {

    public ArquivoProduto() throws Exception {
        super("produto", Produto.class.getConstructor());
        // Usa o arquivo da superclasse
        if (this.arquivo.length() < 4) {
            this.arquivo.writeInt(0);
        }
    }
}
