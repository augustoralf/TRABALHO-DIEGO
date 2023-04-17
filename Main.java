import java.util.Scanner;
import java.math.*;
import java.time.LocalDate;

class Main {
  public static void main(String[] args) {
    Scanner console = new Scanner(System.in);
    String nome, dataAdmissao, cargo;
    int mesReferencia, horasTrabalhadas, diasTrabalhadosSemanal, jornadaHoraSemanal, jornadaHoraMensal, semanas = 5;
    BigDecimal salarioBruto, salarioHora, adicionalPericulosidade, adiconalInsalubridade, valeTransporte, valeAlimentacao, valorINSS, valorFGTS, valorIRRF, salarioLiquido, valorPlanoSaude;
    
    //nome completo
    System.out.println("Insira seu nome: ");
    nome = console.next();
    
    //data de admissao
    System.out.println("Insira a data de admissão (DD/MM/AAAA): ");
    dataAdmissao = console.next();
    
    //mês de referencia
    System.out.println("Insira mês de referencia: \n 1: Janeiro \n 2: Fevereiro \n 3: Março \n 4: Abril \n 5: Maio \n 6: Junho \n 7: Julho \n 8: Agosto \n 9: Setembro \n 10: Outubro \n 11: Novembro \n 12: Dezembro ");
    mesReferencia = console.nextInt();
    LocalDate now = LocalDate.now();
    LocalDate dataReferencia = LocalDate.of(now.year(), mesReferencia, 1);
    semanas = dataReferencia.get(WeekFields.of(locale).weekOfYear());

    //cargo
    System.out.println("Insira seu Cargo: ");
    cargo = console.next();

    //salário bruto
    System.out.println("Insira o Salario Bruto (00.00): ");
    salarioBruto = console.nextBigDecimal();

    //horas trabalhadas por dia
    System.out.println("Informe as horas trabalhadas por dia:");
    horasTrabalhadas = console.nextInt();

    //dias trabalhados semanais
    System.out.println("Informe a quantidade de dias trabalhados semanais:");
    diasTrabalhadosSemanal = console.nextInt();

    jornadaHoraSemanal = horasTrabalhadas * diasTrabalhadosSemanal;
    jornadaHoraMensal = jornadaHoraSemanal * semanas;

    //plano de saúde
    System.out.println("Informe o valor do plano de saúde:");
    valorPlanoSaude = console.nextBigDecimal();
    
    salarioHora = calcularSalarioHora(salarioBruto, console, jornadaHoraMensal);
    adicionalPericulosidade = calcularPericulosidade(salarioBruto);
    adiconalInsalubridade = calcularInsalubridade(console);
    valeTransporte = calcularValeTransporte(salarioBruto, console);
    valeAlimentacao = calcularValeAlimentacao(console, jornadaHoraMensal, horasTrabalhadas);
    valorINSS = calcularValorINSS(salarioBruto);
    valorFGTS = calcularFGTS(salarioBruto);
    valorIRRF = calcularIRRF(salarioBruto, valorINSS, console);
    salarioLiquido = calcularSalarioLiquido(salarioBruto, valorINSS, valorIRRF, valeTransporte, valorPlanoSaude);
    
    //criar relatorio
    System.out.println("\n\n\n\n\n****Folha de Pagamento*****");
    System.out.println("*****Nome Completo: " + nome);
    System.out.println("*****Data de Admissão: " + dataAdmissao);
    System.out.println("*****Mês Referência: " + mesReferencia);
    System.out.println("*****Cargo do colaborador: " + cargo);
    System.out.println("*****Salário do colaborador: " + salarioLiquido);
    System.out.println("\n****Proventos****");
    System.out.println("*****Periculosidade: " + adicionalPericulosidade);
    System.out.println("*****Insalubridade: " + adiconalInsalubridade);
    System.out.println("\n****Descontos****");
    System.out.println("*****INSS: " + valorINSS);
    System.out.println("*****IRRF: " + valorIRRF);
    System.out.println("*****FGTS: " + valorFGTS);
    System.out.println("*****Vale Transporte: " + valeTransporte);
    System.out.println("*****Vale Alimentação: " + valeAlimentacao);
    System.out.println("*****Valor do Salario Bruto: " + salarioBruto);
    System.out.println("*****Valor do Salario Hora: " + salarioHora);
  }

  private static BigDecimal calcularSalarioHora(BigDecimal salarioBruto, Scanner console, int jornadaHoraMensal) {  
    return salarioBruto.divide(new BigDecimal(jornadaHoraMensal)).setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularPericulosidade(BigDecimal salarioBruto) {
    double adicionalPericulosidade = 0.3;
    
    return salarioBruto.multiply(new BigDecimal(adicionalPericulosidade)).setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularInsalubridade(Scanner console) {
    int risco = 0;
    double taxaSalario = 0;
    BigDecimal salarioMinimo = new BigDecimal(1380.60);

    System.out.println("Informe o grau de Risco: \n 1 para: Baixo \n 2 para: Médio \n 3 para: Alto");
    risco = console.nextInt();

    if (risco == 1) {
      taxaSalario = 0.1;
    } else if (risco == 2) {
      taxaSalario = 0.2;
    } else {
      taxaSalario = 0.4;
    }
    
    return salarioMinimo.multiply(new BigDecimal(taxaSalario)).setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularValeTransporte(BigDecimal salarioBruto, Scanner console) {
    BigDecimal valeTransporte, seiscento, taxaTransporte = new BigDecimal(0.06);
    
    System.out.println("Informe o valor do Vale Transporte:");
    valeTransporte = console.nextBigDecimal();

    seiscento = salarioBruto.multiply(taxaTransporte).setScale(2, RoundingMode.HALF_EVEN);

    if (seiscento.compareTo(valeTransporte) == 1) {
      return valeTransporte; 
    } else {
      return seiscento;
    }
  }

  private static BigDecimal calcularValeAlimentacao(Scanner console, int jornadaHoraMensal, int horasTrabalhadas) {
    BigDecimal valorVale; 
    double diasTrabalhados;
    
    System.out.println("Informe o valor diario do Vale Alimentação");
    valorVale = console.nextBigDecimal();

    diasTrabalhados = jornadaHoraMensal / horasTrabalhadas;
       
    return valorVale.multiply(new BigDecimal(diasTrabalhados)).setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularValorINSS(BigDecimal salarioBruto) {
    BigDecimal faixaINSS1 = new BigDecimal(1302.00), faixaINSS2 = new BigDecimal(2571.29), faixaINSS3 = new BigDecimal(3856.94), faixaINSS4 = new BigDecimal(7507.49), res1, res2, res3, res4;
    double ali1 = 0.075, ali2 = 0.09, ali3 = 0.12, ali4 = 0.14;

    if (salarioBruto.compareTo(faixaINSS1) == -1 || salarioBruto.compareTo(faixaINSS1) == 0) {
      res1 = faixaINSS1.multiply(new BigDecimal(ali1));
      res2 = new BigDecimal(0.0);
      res3 = new BigDecimal(0.0);
      res4 = new BigDecimal(0.0);
    } else if (salarioBruto.compareTo(faixaINSS2) == -1 || salarioBruto.compareTo(faixaINSS2) == 0) {
      res1 = faixaINSS1.multiply(new BigDecimal(ali1));
      res2 = faixaINSS2.subtract(faixaINSS1).multiply(new BigDecimal(ali2));
      res3 = new BigDecimal(0.0);
      res4 = new BigDecimal(0.0);
    } else if (salarioBruto.compareTo(faixaINSS3) == -1 || salarioBruto.compareTo(faixaINSS3) == 0) {
      res1 = faixaINSS1.multiply(new BigDecimal(ali1));
      res2 = faixaINSS2.subtract(faixaINSS1).multiply(new BigDecimal(ali2));
      res3 = faixaINSS3.subtract(faixaINSS2).multiply(new BigDecimal(ali3));
      res4 = new BigDecimal(0.0);
    } else {
      res1 = faixaINSS1.multiply(new BigDecimal(ali1));
      res2 = faixaINSS2.subtract(faixaINSS1).multiply(new BigDecimal(ali2));
      res3 = faixaINSS3.subtract(faixaINSS2).multiply(new BigDecimal(ali3));
      res4 = faixaINSS4.subtract(faixaINSS3).multiply(new BigDecimal(ali4));
    }
    
    return res1.add(res2.add(res3.add(res4))).setScale(2, RoundingMode.HALF_EVEN);
  }
    
  private static BigDecimal calcularFGTS(BigDecimal salarioBruto) {
    return salarioBruto.multiply(new BigDecimal(0.08)).setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularIRRF(BigDecimal salarioBruto, BigDecimal valorINSS, Scanner console) {
    BigDecimal baseCalculo, deducaoDepedente = new BigDecimal(189.59), pensaoAlimenticia, outrasDeducoes, totalDeducoes, ded2 = new BigDecimal(142.80), ded3 = new BigDecimal(354.80), ded4 = new BigDecimal(636.13), ded5 = new BigDecimal(869.36), faixa1 = new BigDecimal(1903.98), faixa2 = new BigDecimal(2826.65), faixa3 = new BigDecimal(3751.05), faixa4 = new BigDecimal(4664.48), valorIRRF = BigDecimal.ZERO;
    int dependentes;
    double ali2 = 0.075, ali3 = 0.15, ali4 = 0.225, ali5 = 0.275;
    
    System.out.println("Informe a quantidade de dependetes: ");
    dependentes = console.nextInt();

    System.out.println("Informe a pensão alimentícia: ");
    pensaoAlimenticia = console.nextBigDecimal();

    System.out.println("Informe outras deduções: ");
    outrasDeducoes = console.nextBigDecimal();

    totalDeducoes = valorINSS.add(deducaoDepedente.multiply(new BigDecimal(dependentes))).add(pensaoAlimenticia).add(outrasDeducoes);
    
    baseCalculo = salarioBruto.subtract(totalDeducoes);

    if (baseCalculo.compareTo(faixa1) == 1 && (baseCalculo.compareTo(faixa2) == -1 || baseCalculo.compareTo(faixa2) == 0)) {
      valorIRRF = baseCalculo.multiply(new BigDecimal(ali2)).subtract(ded2);
    } else if (baseCalculo.compareTo(faixa2) == 1 && (baseCalculo.compareTo(faixa3) == -1 || baseCalculo.compareTo(faixa3) == 0)) {
      valorIRRF = baseCalculo.multiply(new BigDecimal(ali3)).subtract(ded3);
    } else if (baseCalculo.compareTo(faixa3) == 1 && (baseCalculo.compareTo(faixa4) == -1 || baseCalculo.compareTo(faixa4) == 0)) {
      valorIRRF = baseCalculo.multiply(new BigDecimal(ali4)).subtract(ded4);
    } else if (baseCalculo.compareTo(faixa4) == 1) {
      valorIRRF = baseCalculo.multiply(new BigDecimal(ali5)).subtract(ded5);
    }
    
    return valorIRRF.setScale(2, RoundingMode.HALF_EVEN);
  }

  private static BigDecimal calcularSalarioLiquido(BigDecimal salarioBruto, BigDecimal valorINSS, BigDecimal valorIRRF, BigDecimal valeTransporte, BigDecimal valorPlanoSaude) {
    return salarioBruto.subtract(valorINSS).subtract(valorIRRF).subtract(valeTransporte).subtract(valorPlanoSaude).setScale(2, RoundingMode.HALF_EVEN);
  }
  
}