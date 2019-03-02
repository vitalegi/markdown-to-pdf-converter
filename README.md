# Converter

## Compilazione ed esecuzione

Il progetto è sviluppato con Java8 + maven + Lombok (1.18.6).

Posizionarsi nella root, quindi eseguire:

```bash
mvn clean package
```

Nella cartella target verrà generato `gantt-pptx-recap-x.y.z-SNAPSHOT.jar`.

Eseguire l'applicazione compilata:

```bash
java -jar target\markdown-to-pdf-converter-0.0.1-SNAPSHOT.jar
```
