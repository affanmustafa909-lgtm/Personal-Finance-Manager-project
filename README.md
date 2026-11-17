# Heritage Ledger — Personal Finance Manager

Classic three-page desktop app in **Java Swing**.

## Pages

1. **Dashboard** — balance, income, expenses, recent activity  
2. **Transactions** — post new ledger entries and view the full register  
3. **Budget & Reports** — category allowances and spending composition  

## Run

```bash
javac -d out src/Main.java src/model/*.java src/ui/*.java
java -cp out Main
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force -Path out | Out-Null
javac -d out src/Main.java src/model/*.java src/ui/*.java
java -cp out Main
```

Requires JDK 17+ (uses `instanceof` pattern matching).
