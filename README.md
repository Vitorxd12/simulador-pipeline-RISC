# 🧠 Simulador de Pipeline RISC

Usando como base o modelo de instrução RISC, é proposto a criação de um simulador de pipeline em 5 estágios. O trabalho deve observar as características gerais do modelo, seguir o padrão de processamento e respeitar as diretrizes de cada estágio. Características gerais do projeto serão descritas a seguir.

## 📌 Visão geral do Projeto

- O simulador deve ser feito em **Java**.
- O conteúdo da memória deve ser carregado a partir de um arquivo.
- O conteúdo dos registradores deve ser carregado a partir de um arquivo.
- As instruções devem ser carregadas a partir de um arquivo.

## ⚙️ Operadores Principais

- `ADD` – Soma valores e salva em um registrador  
  Exemplo: `add r0 r1 r2`

- `SUB` – Subtrai valores e salva em um registrador  
  Exemplo: `sub r0 r1 r2`

- `LW` – Carrega o conteúdo de uma memória em um registrador  
  Exemplo: `lw rs2, offset(rs1)`

- `SW` – Armazena o conteúdo do registrador em uma memória  
  Exemplo: `sw rs2, offset(rs1)`

## 🚧 Operadores Desafio

- `SWAP` – Troca os valores dos registradores  
  Exemplo: `swap r0 r1`

- `AVG` – Calcula a média entre dois registradores  
  Exemplo: `avg r0 r1 r2`

- `REV` – Reverte a ordem dos bits do registrador  
  Exemplo: `rev r0 r1`  
  Considerando que são armazenados inteiros de 32 bits em cadeias de 8 bits cada.

  ```
  Entrada:
    00000000 00000000 00000000 00001001
    00000000 00000000 00000000 00001111
    00000000 00000000 00000000 10000000
    00000000 00000000 00000000 00000001
    11100000 00000000 00000000 00000000
  ```
  ```
  Saída:
    10010000 00000000 00000000 00000000
    11110000 00000000 00000000 00000000
    00000001 00000000 00000000 00000000
    10000000 00000000 00000000 00000000
    00000000 00000000 00000000 00000111
  ```


## 🧩 Visão geral dos componentes

### Simulador.java 
Classe central que gerencia o fluxo entre os estágios do pipeline. Responsável por inicializar, avançar os ciclos, controlar os registradores entre estágios e coletar estatísticas.

### Estágios (Stages)

Cada estágio (**IF**, **ID**, **EX**, **MEM**, **WB**) tem sua lógica encapsulada em uma classe ou método próprio, recebendo e repassando informações para o próximo estágio.

- `IF` – Instruction Fetch  
- `ID` – Instruction Decode  
- `EX` – Execute  
- `MEM` – Memory Access  
- `WB` – Write Back  

### Main.java

Ponto de entrada do programa. Inicializa o simulador e simula a execução de instruções com base em um arquivo carregado.

## 📝 Notas importantes

- Um **Parser** deve ser usado pelo simulador para inicializar a lista de instruções.
- Os arquivos com instruções, memória e registradores estão no repositório.
- O trabalho deve ser feito **em grupo**.

## 📅 Prazo

Entrega até **27/08**
  
