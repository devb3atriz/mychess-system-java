package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    //regras do jogo de xadrez

    private int turn;
    private Color currentPlayer;
    private Board board;

    public ChessMatch() {
        //dimensão do tababuleiro
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }
    
    public int getTurn(){
        return turn;
    }
    
    public Color getCurrentPlayer(){
        return currentPlayer;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }
    
    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    //mover a peça de sua posição de origem para posição de destino:
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        //validação da peça na posição de origem
        validateSourcePosition(source);

        //validação da peça na posição de destino
        validateTargetPosition(source, target);
        
        //resultado da operação makeMove (método p/ realizar o movimento da peça)
        Piece capturedPiece = makeMove(source, target);
        
        //trocar o turno
        nextTurn();
        
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        return capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException ("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }
    
    private void validateTargetPosition(Position source, Position target){
        if (!board.piece(source).possibleMove(target)){
            throw new ChessException ("The chosen piece can't move to target position");
        }
    }
    
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(Color.WHITE, board));
        placeNewPiece('c', 2, new Rook(Color.WHITE, board));
        placeNewPiece('d', 2, new Rook(Color.WHITE, board));
        placeNewPiece('e', 2, new Rook(Color.WHITE, board));
        placeNewPiece('e', 1, new Rook(Color.WHITE, board));
        placeNewPiece('d', 1, new King(Color.WHITE, board));

        placeNewPiece('c', 7, new Rook(Color.BLACK, board));
        placeNewPiece('c', 8, new Rook(Color.BLACK, board));
        placeNewPiece('d', 7, new Rook(Color.BLACK, board));
        placeNewPiece('e', 7, new Rook(Color.BLACK, board));
        placeNewPiece('e', 8, new Rook(Color.BLACK, board));
        placeNewPiece('d', 8, new King(Color.BLACK, board));
    }
}
