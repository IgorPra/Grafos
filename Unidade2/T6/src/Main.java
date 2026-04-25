package src;

public class Main {
    public static void main(String[] args) {
        String file1 = "dados/iso-path4-a.txt";
        String file2 = "dados/iso-path4-b.txt";

        String file3 = "dados/nao-iso-estrela5.txt";
        String file4 = "dados/nao-iso-path5.txt";

        String file5 = "dados/unico-centro-a.txt";
        String file6 = "dados/unico-centro-b.txt";

        Graph tree1 = new Graph(new In(file5));
        Graph tree2 = new Graph(new In(file6));

        TreeIsomorphism analysis1 = new TreeIsomorphism(tree1);
        TreeIsomorphism analysis2 = new TreeIsomorphism(tree2);

        StdOut.println("Arvore 1");
        StdOut.println("Lista de adjacencia:");
        StdOut.println(tree1);
        StdOut.println(analysis1.getValidationMessage());

        StdOut.println();
        StdOut.println("Arvore 2");
        StdOut.println("Lista de adjacencia:");
        StdOut.println(tree2);
        StdOut.println(analysis2.getValidationMessage());

        if (!analysis1.isTree() || !analysis2.isTree()) {
            StdOut.println();
            StdOut.println("Resultado: nao foi possivel comparar, pois uma das entradas nao eh arvore.");
            return;
        }

        String code1 = analysis1.getCanonicalEncoding();
        String code2 = analysis2.getCanonicalEncoding();

        StdOut.println();
        StdOut.println("Centros arvore 1: " + analysis1.centersAsString());
        StdOut.println("Centros arvore 2: " + analysis2.centersAsString());
        StdOut.println("Codigo arvore 1: " + code1);
        StdOut.println("Codigo arvore 2: " + code2);

        StdOut.println();
        if (code1.equals(code2)) {
            StdOut.println("Resultado: as arvores sao isomorfas.");
        } else {
            StdOut.println("Resultado: as arvores nao sao isomorfas.");
        }
    }
}