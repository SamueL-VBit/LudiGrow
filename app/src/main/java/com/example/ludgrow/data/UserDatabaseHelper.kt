// Caminho do arquivo: app/src/main/java/com/example/ludgrow/data/UserDatabaseHelper.kt
package com.example.ludgrow.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LudiGrow.db"
        // 1. VERSÃO DO BANCO ATUALIZADA
        private const val DATABASE_VERSION = 3

        // Tabela de Usuários
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
        private const val COLUMN_USER_TYPE = "user_type"

        // Tabela de Crianças
        private const val TABLE_CRIANCAS = "criancas"
        private const val COLUMN_CRIANCA_ID = "id"
        private const val COLUMN_CRIANCA_NOME = "nome"
        private const val COLUMN_CRIANCA_DATA_NASCIMENTO = "data_nascimento"
        private const val COLUMN_CRIANCA_NIVEL_SUPORTE = "nivel_suporte"
        private const val COLUMN_CRIANCA_OBSERVACOES = "observacoes"
        private const val COLUMN_ID_DO_PAI = "id_do_pai"
        private const val COLUMN_ID_DO_AT = "id_do_at"

        // 2. CONSTANTES DA NOVA TABELA DE ATIVIDADES
        private const val TABLE_ATIVIDADES = "atividades"
        private const val COLUMN_ATIVIDADE_ID = "id"
        private const val COLUMN_ATIVIDADE_NOME = "nome"
        private const val COLUMN_ATIVIDADE_DESCRICAO = "descricao"
        private const val COLUMN_ATIVIDADE_AREA = "area_desenvolvimento"
        private const val COLUMN_ATIVIDADE_DATA = "data_realizacao"
        private const val COLUMN_ID_DA_CRIANCA = "id_da_crianca"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_EMAIL TEXT UNIQUE,
                $COLUMN_USER_PASSWORD TEXT,
                $COLUMN_USER_TYPE TEXT
            )
        """.trimIndent()

        val createCriancaTable = """
            CREATE TABLE $TABLE_CRIANCAS (
                $COLUMN_CRIANCA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CRIANCA_NOME TEXT,
                $COLUMN_CRIANCA_DATA_NASCIMENTO TEXT,
                $COLUMN_CRIANCA_NIVEL_SUPORTE TEXT,
                $COLUMN_CRIANCA_OBSERVACOES TEXT,
                $COLUMN_ID_DO_PAI INTEGER,
                $COLUMN_ID_DO_AT INTEGER,
                FOREIGN KEY($COLUMN_ID_DO_PAI) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                FOREIGN KEY($COLUMN_ID_DO_AT) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        val createAtividadeTable = """
            CREATE TABLE $TABLE_ATIVIDADES (
                $COLUMN_ATIVIDADE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ATIVIDADE_NOME TEXT,
                $COLUMN_ATIVIDADE_DESCRICAO TEXT,
                $COLUMN_ATIVIDADE_AREA TEXT,
                $COLUMN_ATIVIDADE_DATA TEXT,
                $COLUMN_ID_DA_CRIANCA INTEGER,
                FOREIGN KEY($COLUMN_ID_DA_CRIANCA) REFERENCES $TABLE_CRIANCAS($COLUMN_CRIANCA_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createCriancaTable)
        db.execSQL(createAtividadeTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createCriancaTable = """
                CREATE TABLE $TABLE_CRIANCAS (
                    $COLUMN_CRIANCA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_CRIANCA_NOME TEXT,
                    $COLUMN_CRIANCA_DATA_NASCIMENTO TEXT,
                    $COLUMN_CRIANCA_NIVEL_SUPORTE TEXT,
                    $COLUMN_CRIANCA_OBSERVACOES TEXT,
                    $COLUMN_ID_DO_PAI INTEGER,
                    $COLUMN_ID_DO_AT INTEGER,
                    FOREIGN KEY($COLUMN_ID_DO_PAI) REFERENCES $TABLE_USERS($COLUMN_USER_ID),
                    FOREIGN KEY($COLUMN_ID_DO_AT) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
                )
            """.trimIndent()
            db.execSQL(createCriancaTable)
        }

        // 3. NOVO BLOCO PARA CRIAR A TABELA 'atividades'
        if (oldVersion < 3) {
            val createAtividadeTable = """
                CREATE TABLE $TABLE_ATIVIDADES (
                    $COLUMN_ATIVIDADE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_ATIVIDADE_NOME TEXT,
                    $COLUMN_ATIVIDADE_DESCRICAO TEXT,
                    $COLUMN_ATIVIDADE_AREA TEXT,
                    $COLUMN_ATIVIDADE_DATA TEXT,
                    $COLUMN_ID_DA_CRIANCA INTEGER,
                    FOREIGN KEY($COLUMN_ID_DA_CRIANCA) REFERENCES $TABLE_CRIANCAS($COLUMN_CRIANCA_ID) ON DELETE CASCADE
                )
            """.trimIndent()
            db.execSQL(createAtividadeTable)
        }
    }

    // --- Funções de Usuário ---
    fun addUser(email: String, password: String, userType: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_PASSWORD, password)
            put(COLUMN_USER_TYPE, userType)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getUserId(email: String, password: String, userType: String): Int? {
        val db = this.readableDatabase
        val selection = "$COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ? AND $COLUMN_USER_TYPE = ?"
        val selectionArgs = arrayOf(email, password, userType)
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID), selection, selectionArgs, null, null, null)

        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    // --- Funções de Criança (CRUD) ---
    fun addCrianca(crianca: Crianca): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CRIANCA_NOME, crianca.nome)
            put(COLUMN_CRIANCA_DATA_NASCIMENTO, crianca.dataNascimento)
            put(COLUMN_CRIANCA_NIVEL_SUPORTE, crianca.nivelSuporte)
            put(COLUMN_CRIANCA_OBSERVACOES, crianca.observacoes)
            put(COLUMN_ID_DO_PAI, crianca.idDoPai)
        }
        val result = db.insert(TABLE_CRIANCAS, null, values)
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getCriancasDoUsuario(userId: Int, userType: String): List<Crianca> {
        val criancas = mutableListOf<Crianca>()
        val db = this.readableDatabase
        val columnToFilter = if (userType == "Pai") COLUMN_ID_DO_PAI else COLUMN_ID_DO_AT
        val selection = "$columnToFilter = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor = db.query(TABLE_CRIANCAS, null, selection, selectionArgs, null, null, null)

        while (cursor.moveToNext()) {
            val crianca = Crianca(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_CRIANCA_ID)),
                nome = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_NOME)),
                dataNascimento = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_DATA_NASCIMENTO)),
                nivelSuporte = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_NIVEL_SUPORTE)),
                observacoes = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_OBSERVACOES)),
                idDoPai = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DO_PAI)),
                idDoAt = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DO_AT))
            )
            criancas.add(crianca)
        }
        cursor.close()
        db.close()
        return criancas
    }

    @SuppressLint("Range")
    fun getSingleCrianca(criancaId: Int): Crianca? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_CRIANCAS, null, "$COLUMN_CRIANCA_ID = ?", arrayOf(criancaId.toString()), null, null, null)

        var crianca: Crianca? = null
        if (cursor.moveToFirst()) {
            crianca = Crianca(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_CRIANCA_ID)),
                nome = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_NOME)),
                dataNascimento = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_DATA_NASCIMENTO)),
                nivelSuporte = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_NIVEL_SUPORTE)),
                observacoes = cursor.getString(cursor.getColumnIndex(COLUMN_CRIANCA_OBSERVACOES)),
                idDoPai = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DO_PAI)),
                idDoAt = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DO_AT))
            )
        }
        cursor.close()
        db.close()
        return crianca
    }

    fun updateCrianca(crianca: Crianca): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CRIANCA_NOME, crianca.nome)
            put(COLUMN_CRIANCA_DATA_NASCIMENTO, crianca.dataNascimento)
            put(COLUMN_CRIANCA_NIVEL_SUPORTE, crianca.nivelSuporte)
            put(COLUMN_CRIANCA_OBSERVACOES, crianca.observacoes)
        }
        val result = db.update(TABLE_CRIANCAS, values, "$COLUMN_CRIANCA_ID = ?", arrayOf(crianca.id.toString()))
        db.close()
        return result
    }

    fun deleteCrianca(criancaId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CRIANCAS, "$COLUMN_CRIANCA_ID = ?", arrayOf(criancaId.toString()))
        db.close()
        return result
    }

    // 4. NOVA FUNÇÃO PARA ADICIONAR ATIVIDADE
    fun addAtividade(atividade: Atividade): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ATIVIDADE_NOME, atividade.nome)
            put(COLUMN_ATIVIDADE_DESCRICAO, atividade.descricao)
            put(COLUMN_ATIVIDADE_AREA, atividade.areaDesenvolvimento)
            put(COLUMN_ATIVIDADE_DATA, atividade.dataRealizacao)
            put(COLUMN_ID_DA_CRIANCA, atividade.idDaCrianca)
        }
        val result = db.insert(TABLE_ATIVIDADES, null, values)
        db.close()
        return result
    }
    // Em: app/src/main/java/com/example/ludgrow/data/UserDatabaseHelper.kt

// ... (dentro da classe UserDatabaseHelper, após a função addAtividade)

    @SuppressLint("Range")
    fun getAtividadesDaCrianca(criancaId: Int): List<Atividade> {
        val atividades = mutableListOf<Atividade>()
        val db = this.readableDatabase
        val selection = "$COLUMN_ID_DA_CRIANCA = ?"
        val selectionArgs = arrayOf(criancaId.toString())

        val cursor = db.query(
            TABLE_ATIVIDADES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COLUMN_ATIVIDADE_ID DESC" // Ordena pelas mais recentes primeiro
        )

        while (cursor.moveToNext()) {
            val atividade = Atividade(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ATIVIDADE_ID)),
                nome = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_NOME)),
                descricao = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_DESCRICAO)),
                areaDesenvolvimento = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_AREA)),
                dataRealizacao = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_DATA)),
                idDaCrianca = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DA_CRIANCA))
            )
            atividades.add(atividade)
        }
        cursor.close()
        db.close()
        return atividades
    }
    // Em: app/src/main/java/com/example/ludgrow/data/UserDatabaseHelper.kt

// ... (dentro da classe UserDatabaseHelper, após a função getAtividadesDaCrianca)

    fun deleteAtividade(atividadeId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_ATIVIDADES,
            "$COLUMN_ATIVIDADE_ID = ?",
            arrayOf(atividadeId.toString())
        )
        db.close()
        return result
    }

    // Em: app/src/main/java/com/example/ludgrow/data/UserDatabaseHelper.kt

// ... (dentro da classe UserDatabaseHelper, após a função deleteAtividade)

    fun updateAtividade(atividade: Atividade): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ATIVIDADE_NOME, atividade.nome)
            put(COLUMN_ATIVIDADE_DESCRICAO, atividade.descricao)
            put(COLUMN_ATIVIDADE_AREA, atividade.areaDesenvolvimento)
            put(COLUMN_ATIVIDADE_DATA, atividade.dataRealizacao)
        }

        val result = db.update(
            TABLE_ATIVIDADES,
            values,
            "$COLUMN_ATIVIDADE_ID = ?",
            arrayOf(atividade.id.toString())
        )
        db.close()
        return result
    }

    // Em: app/src/main/java/com/example/ludgrow/data/UserDatabaseHelper.kt

// ... (dentro da classe, pode ser antes de updateAtividade)

    @SuppressLint("Range")
    fun getSingleAtividade(atividadeId: Int): Atividade? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_ATIVIDADES,
            null,
            "$COLUMN_ATIVIDADE_ID = ?",
            arrayOf(atividadeId.toString()),
            null, null, null
        )

        var atividade: Atividade? = null
        if (cursor.moveToFirst()) {
            atividade = Atividade(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ATIVIDADE_ID)),
                nome = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_NOME)),
                descricao = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_DESCRICAO)),
                areaDesenvolvimento = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_AREA)),
                dataRealizacao = cursor.getString(cursor.getColumnIndex(COLUMN_ATIVIDADE_DATA)),
                idDaCrianca = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_DA_CRIANCA))
            )
        }
        cursor.close()
        db.close()
        return atividade
    }




}
