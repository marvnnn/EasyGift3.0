package Arquivos;

import Entidades.*;
import aed3.Arquivo;

public class ArquivoLista extends Arquivo<Lista> {

    public ArquivoLista() throws Exception {
        super("lista", Lista.class.getConstructor());
        // Usa o arquivo da superclasse
        if (this.arquivo.length() < 4) {
            this.arquivo.writeInt(0);
        }
    }
}
