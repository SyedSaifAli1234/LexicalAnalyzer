package Compiler;

public class TLPair {
	private String token;
	private String lexeme;

	public TLPair(String token, String lexeme) {
		super();
		this.token = token;
		this.lexeme = lexeme;
	}
	
    public String toString() {
        return "(" + token + ", " + lexeme + ")";
    }

    public String getLexeme()
    {
    	return lexeme;
    }
    
    public String getToken()
    {
    	return token;
    }
}
