
import java.util.ArrayList;
import java.util.List;

public final class Tab {

    private Tab tabuleiroPai;
    private int[][] estado;
    private List<Tab> escolhas = new ArrayList<>();
    private int heuristica = 0;
    private int nivel;
    private static final int tamanho = 3;

    public Tab(Tab tabuleiroPai, int[][] estado) {
        this.tabuleiroPai = tabuleiroPai;
        this.setEstado(estado);
        nivel = tabuleiroPai.getNivel() + 1;
    }

    public Tab(int[][] estado) {
        this.setEstado(estado);
        nivel = 1;
    }

    public Tab() {
        estado = new int[tamanho][tamanho];
        nivel = 1;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<Tab> getEscolhas() {
        return escolhas;
    }

    public void setEscolhas(List<Tab> escolhas) {
        this.escolhas = escolhas;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }

    public Tab getTabuleiroPai() {
        return tabuleiroPai;
    }

    public void setTabuleiroPai(Tab tabuleiroPai) {
        this.tabuleiroPai = tabuleiroPai;
    }

    public int[][] getEstado() {
        return estado;
    }

    public void setEstado(int[][] estado) {
        int posicaoPreenchida[] = new int[tamanho * tamanho];
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                posicaoPreenchida[estado[i][j]] += 1;
            }
        }
        this.estado = estado;
    }

    public boolean isEqual(int[][] estadoComparar) {

        return estado[0][0] == estadoComparar[0][0]
                && estado[0][1] == estadoComparar[0][1]
                && estado[0][2] == estadoComparar[0][2]
                && estado[1][0] == estadoComparar[1][0]
                && estado[1][1] == estadoComparar[1][1]
                && estado[1][2] == estadoComparar[1][2]
                && estado[2][0] == estadoComparar[2][0]
                && estado[2][1] == estadoComparar[2][1]
                && estado[2][2] == estadoComparar[2][2];
    }

    public boolean isEqual(Tab t) {
        return isEqual(t.getEstado());
    }

    public int[][] clona() {
        int clone[][] = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            clone[i] = estado[i].clone();
        }

        return clone;
    }

    public List<Tab> retornaFilhos() {
        List<Tab> filhos = new ArrayList<>();

        int linha = 0;
        int coluna = 0;

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (estado[i][j] == 0) {
                    linha = i;
                    coluna = j;
                    break;
                }
            }
        }

        if (linha - 1 >= 0) {
            int copia[][] = this.clona();
            copia[linha][coluna] = estado[linha - 1][coluna];
            copia[linha - 1][coluna] = 0;
            Tab filho = new Tab(this, copia);
            if (!verificaRepeticao(filho)) {
                filhos.add(filho);
            }
        }

        if (linha + 1 <= tamanho - 1) {
            int copia[][] = this.clona();
            copia[linha][coluna] = estado[linha + 1][coluna];
            copia[linha + 1][coluna] = 0;
            Tab filho = new Tab(this, copia);
            if (!verificaRepeticao(filho)) {
                filhos.add(filho);
            }
        }

        if (coluna - 1 >= 0) {
            int copia[][] = this.clona();
            copia[linha][coluna] = estado[linha][coluna - 1];
            copia[linha][coluna - 1] = 0;
            Tab filho = new Tab(this, copia);
            if (!verificaRepeticao(filho)) {
                filhos.add(filho);
            }
        }

        if (coluna + 1 <= tamanho - 1) {
            int copia[][] = this.clona();
            copia[linha][coluna] = estado[linha][coluna + 1];
            copia[linha][coluna + 1] = 0;
            Tab filho = new Tab(this, copia);
            if (!verificaRepeticao(filho)) {
                filhos.add(filho);
            }
        }

        return filhos;
    }

    public Tab RetornaMenorFilho(Tab tabDesejado) {
        Tab menorFilho = null;
        for (Tab tFilho : this.retornaFilhos()) {
            if (menorFilho == null || tFilho.calculaHeuristica(tabDesejado) < menorFilho.calculaHeuristica(tabDesejado)) {
                menorFilho = tFilho;
            }
        }
        if (menorFilho == null) {
            menorFilho = this.getTabuleiroPai();
        } else {
            this.escolhas.add(menorFilho);
            repetidos.add(menorFilho);
        }
        return menorFilho;
    }

    @Override
    public String toString() {
        String retorno = "";
        for (int i = 0; i < tamanho; i++) {
            retorno += " | ";
            for (int j = 0; j < tamanho; j++) {
                retorno += estado[i][j] == 0 ? "*" : estado[i][j];
                retorno += " | ";
            }
            retorno += "\n";
        }

        retorno += "\nPosicao: " + nivel;
        return retorno;
    }

    public int[] getPosicao(int elemento) {
        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (estado[i][j] == elemento) {
                    int[] ret = {i, j};
                    return ret;
                }
            }
        }
        return null;
    }

    public int calculaHeuristica(Tab estadoDesejado) {
        if (heuristica != 0) {
            return heuristica;
        }
        int[][] estadoA = this.getEstado();
        int[][] estadoB = estadoDesejado.getEstado();
        int soma = 0;
        for (int i = 0; i < estadoA.length; i++) {
            for (int j = 0; j < estadoA.length; j++) {
                if (estadoA[i][j] > 0 && estadoA[i][j] != estadoB[i][j]) {
                    soma += 1;
                    int posA[] = this.getPosicao(estadoA[i][j]);
                    int posB[] = estadoDesejado.getPosicao(estadoA[i][j]);
                    soma += Math.abs(posA[0] - posB[0]) + Math.abs(posA[1] - posB[1]);
                }
            }
        }
        heuristica = soma;
        return soma;
    }

    public static List<Tab> repetidos = new ArrayList<>();

    private boolean verificaRepeticao(Tab filho) {

        if (this.getTabuleiroPai() != null && filho.isEqual(this.getTabuleiroPai())) {
            return true;
        }

        for (Tab tRepetido : this.getEscolhas()) {
            if (filho.isEqual(tRepetido)) {
                return true;
            }
        }

        Tab testeRepetido;

        for (int i = repetidos.size() - 1; i >= 0; i--) {
            testeRepetido = repetidos.get(i);
            if (filho.isEqual(testeRepetido)) {
                return true;
            }
        }

        return false;
    }

}
