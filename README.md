# 家計簿アプリ

## 概要

日々の支出と月ごとの予算を管理するWebアプリケーションです。

支出の登録・更新・削除・カテゴリによる絞り込みや、月ごとの予算登録ができます。

Spring Bootを使用して開発し、月予算処理におけるController・Service・Repositoryの責務分離、Bean Validationによる入力値検証、自動テスト、GitHub ActionsによるCIを実装しています。

## 画面イメージ

### 支出登録画面
<img width="535" height="456" alt="image" src="https://github.com/user-attachments/assets/3845f97d-e482-4c50-88af-dfb4254eb444" />

### 支出一覧・予算確認画面
<img width="865" height="344" alt="image" src="https://github.com/user-attachments/assets/5b7c2b88-7550-444f-905a-5f8b9b2b0d7a" />

## 主な機能

- 支出の登録
- 支出の更新
- 支出の削除
- 支出のカテゴリごとの絞り込み
- 月毎の予算の登録
- 月毎の支出と予算を比較し、予算超過を確認

## 使用技術

- Java
- Spring Boot
  - Spring Web MVC
  - Thymeleaf
  - Spring Data JPA
  - Bean Validation
- H2 Database
- Maven
- Git / GitHub
- GitHub Actions

## アーキテクチャ

```text
Browser
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

## テスト

Repository・Service・Controllerなどの各層に対して自動テストを実装しています。

- Repository：支出の保存・取得、カテゴリ絞り込みと並び順を検証
- Service：月予算の新規登録・既存予算更新の業務ロジックを検証
- Controller：HTTPリクエスト、Validation、リダイレクト、依存処理の呼び出しを検証
- Application Context：Spring Bootの設定を正常に読み込めることを検証

現在8件の自動テストを実装しています。

## CI

GitHub Actionsを使用してCIを構築しています。

GitHubへのpushやPull Requestを契機にMavenで自動テストを実行し、コード変更によって既存機能が壊れていないかを自動で確認します。

これにより、テストの実行忘れを防ぎ、不具合を早期に検知できるようにしています。

## セットアップ

### 前提環境

- Java 25
- Git

### 起動方法

1. リポジトリをクローンします。

```bash
git clone https://github.com/nk-java/household-budget.git
```

2. プロジェクトのフォルダへ移動します。

```bash
cd household-budget
```

3. Spring Bootアプリケーションを起動します。

```bash
.\mvnw.cmd spring-boot:run
```

4. ブラウザで以下にアクセスします。

```text
http://localhost:8080
```

## Git開発フロー

機能追加や修正は、mainブランチから作業ブランチを作成して進めています。

```text
main
  ↓
作業ブランチ作成
  ↓
実装・修正
  ↓
git add
  ↓
git commit
  ↓
git push
  ↓
Pull Request
  ↓
GitHub ActionsでCI実行
  ↓
レビュー・確認
  ↓
mainへマージ
```

## 設計上の工夫

- 月予算に関する業務ロジックをService層へ分離し、Controllerが複雑になりすぎないようにしています。
- Repository層にデータベースアクセスを集約し、画面制御や業務ロジックと分離しています。
- Bean Validationを使用し、サーバー側で入力値を検証しています。
- Repository・Service・Controllerごとに自動テストを作成し、各層の責務を個別に確認できるようにしています。
- GitHub ActionsによるCIを導入し、pushやPull Request時に自動テストを実行しています。

## 今後の改善点

- 支出処理にもService層を導入し、Controllerから業務ロジックをさらに分離する
- H2 DatabaseからPostgreSQLへ移行し、より実運用に近いデータベース構成にする
- 例外処理を実装し、想定外のエラーが発生した場合にも適切な画面やメッセージを返せるようにする
- Dockerを導入し、開発環境を再現しやすくする
- 自動テストを拡充し、異常系や境界値も検証する
- クラウド環境へデプロイし、外部から利用できる状態にする

## プロジェクト構成

```text
src/main/java/com/example/household_budget/
├── controller/
│   └── HomeController.java
├── form/
│   └── ExpenseForm.java
├── model/
│   ├── Expense.java
│   └── MonthlyBudget.java
├── repository/
│   ├── ExpenseRepository.java
│   └── MonthlyBudgetRepository.java
└── service/
    └── MonthlyBudgetService.java
```
