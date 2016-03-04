import java.io.IOException;

public class Operacoes {
    
    public static void main(String[] args) throws IOException {
        int inicial[][] = {{5,6,7},{4,0,1},{2,3,8}};
	int fim[][] = {{1,2,3},{8,0,4},{7,6,5}};
                    
        Tab estadoInicial      = new Tab(inicial);
        Tab desejado = new Tab(fim);
        Tab tabuleiro            = estadoInicial;
        
        Tab.repetidos.add(estadoInicial);
        
        do{
            tabuleiro = tabuleiro.RetornaMenorFilho(desejado);
                if (tabuleiro.isEqual(desejado)){
                    break;
                }
        }while(true);
                 
        while(tabuleiro.getTabuleiroPai() != null){
            tabuleiro = tabuleiro.getTabuleiroPai();
        }
        while(tabuleiro != null){
            
            if (tabuleiro != null){
            tabuleiro = tabuleiro.getEscolhas().size() - 1 >= 0 ? tabuleiro.getEscolhas().get(tabuleiro.getEscolhas().size() - 1) : null;
                System.out.println(tabuleiro);
                }
            
        }
    }
}
