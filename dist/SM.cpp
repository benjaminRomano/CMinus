// Emulator for stack machine code (SM)
// Written in C++
// by Richard Borie
// Last revised March 2000

#include <iostream>
#include <fstream>
#include <cctype>
#include <string>
#include <cstring>
#include <stdlib.h>
using namespace std;

int tablesize=0, progsize=0, maxlevel=0, pc=0, sp=0, d[10], m[1000];
bool trace;

static struct {
	char *opcode;
	int instr;
	int arity;
} table[50];

static struct {
	char label[10];
	char opcode[10];
	int instr;
	char arg1[10];
	int a1;
	char arg2[10];
	int a2;
} prog[1000];

enum {ADD, SUB, MULT, DIV, MOD, AND, OR, NEG, NOT, EQ, NE, LT, GT, LE, GE, IN, OUT, NOP,
	LDC, LDA, LDV, LDI, ST, STI, JMP, JMPF, JMPT, INIT, HALT, ALLOC, CALL, PROC, RET,
	ENTER, EXIT, DUP, POP, STL, IND, INCH, OUTCH, OUTSTR};

void insert(char *opcode, int instr, int arity) {
	table[tablesize].opcode=opcode;
	table[tablesize].instr=instr;
	table[tablesize].arity=arity;
	tablesize++;
}

int lookup(char *opcode) {
	for (int k=0; k<tablesize; k++)
		if (strcmp(opcode,table[k].opcode)==0)
			return k;
	return -1;
}

int findlabel(char *label) {
	for (int k=0; k<progsize; k++)
		if (strcmp(label,prog[k].label)==0)
			return k;
	return -1;
}

int main(int argc, char * argv[ ]) {
	insert("ADD",ADD,0);
	insert("SUB",SUB,0);
	insert("MULT",MULT,0);
	insert("DIV",DIV,0);
	insert("MOD",MOD,0);
	insert("AND",AND,0);
	insert("OR",OR,0);
	insert("NEG",NEG,0);
	insert("NOT",NOT,0);
	insert("EQ",EQ,0);
	insert("NE",NE,0);
	insert("LT",LT,0);
	insert("GT",GT,0);
	insert("LE",LE,0);
	insert("GE",GE,0);
	insert("IN",IN,0);
	insert("OUT",OUT,0);
	insert("NOP",NOP,0);
	insert("LDC",LDC,1);
	insert("LDA",LDA,2);
	insert("LDV",LDV,2);
	insert("LDI",LDI,2);
	insert("ST",ST,2);
	insert("STI",STI,2);
	insert("JMP",JMP,1);
	insert("JMPF",JMPF,1);
	insert("JMPT",JMPT,1);
	insert("INIT",INIT,0);
	insert("HALT",HALT,0);
	insert("ALLOC",ALLOC,1);
	insert("CALL",CALL,1);
	insert("PROC",PROC,1);
	insert("RET",RET,1);
	insert("ENTER",ENTER,1);
	insert("EXIT",EXIT,1);
	insert("DUP",DUP,0);
	insert("POP",POP,0);
	insert("STL",STL,0);
	insert("IND",IND,0);
	insert("INCH",INCH,0);
	insert("OUTCH",OUTCH,0);
	insert("OUTSTR",OUTSTR,1);

	char file[20], str[10];
	int temp;

	if (argc==2) {
		trace=false;
		strcpy(file, argv[1]);
	}
	else if (argc==3) {
		trace=true;
		strcpy(file, argv[2]);
	}
	else {
		cerr << "Usage: " << argv[0] << " [-trace] filename.sm" << endl;
		exit(1);
	}
	
	ifstream f(file, ios::in);
	if (!f) {
		cerr << "File " << file << " not found" << endl;
		exit(1);
	}

	while (!f.eof( )) {
		f >> str;
		temp=lookup(str);
		if (temp<0) {
			strcpy(prog[progsize].label,str);
			f >> str;
			temp=lookup(str);
		}
		else
			strcpy(prog[progsize].label,"");
		strcpy(prog[progsize].opcode,str);
		prog[progsize].instr=temp;
		if (table[temp].arity>0) {
			f >> str;
			strcpy(prog[progsize].arg1,str);
		}
		else prog[progsize].a1=0;
		if (table[temp].arity>1) {
			f >> str;
			strcpy(prog[progsize].arg2,str);
		}
		else prog[progsize].a2=0;
		progsize++;
	}

	for (int j=0; j<progsize; j++) {
		if (!isalpha(prog[j].arg1[0]))
			prog[j].a1=atoi(prog[j].arg1);
		else
			prog[j].a1=findlabel(prog[j].arg1);
		if (!isalpha(prog[j].arg2[0]))
			prog[j].a2=atoi(prog[j].arg2);
		else
			prog[j].a2=findlabel(prog[j].arg2);
	}
		
	pc=0;
	while (true) {
		if (trace) {
			cout << endl;
			int k;
			for (k=maxlevel;k>=0;k--)
				cout << "d[" << k << "]=" << d[k] << ". ";
			cout << endl << endl;
			for (k=sp;k>0;k--)
				cout << "m[" << k << "]=" << m[k] << ". ";
			cout << endl << endl;
			cout << pc << "\t" << prog[pc].label << "\t" << prog[pc].opcode << "\t"
				 << prog[pc].arg1 << "\t" << prog[pc].arg2 << endl << endl;
			cout << "Press 'Enter' to proceed:" << endl;
			char c=cin.get( );
		}

		int instr=prog[pc].instr, a1=prog[pc].a1, a2=prog[pc].a2;
		pc++;

		switch (instr) {
		case ADD:
			m[sp-1]+=m[sp];
			sp--;
			break;
		case SUB:
			m[sp-1]-=m[sp];
			sp--;
			break;
		case MULT:
			m[sp-1]*=m[sp];
			sp--;
			break;
		case DIV:
			m[sp-1]/=m[sp];
			sp--;
			break;
		case MOD:
			m[sp-1]%=m[sp];
			sp--;
			break;
		case AND:
			m[sp-1]=m[sp-1]&&m[sp];
			sp--;
			break;
		case OR:
			m[sp-1]=m[sp-1]||m[sp];
			sp--;
			break;
		case NEG:
			m[sp]= -m[sp];
			break;
		case NOT:
			m[sp]= !m[sp];
			break;
		case EQ:
			m[sp-1]=m[sp-1]==m[sp];
			sp--;
			break;
		case NE:
			m[sp-1]=m[sp-1]!=m[sp];
			sp--;
			break;
		case LT:
			m[sp-1]=m[sp-1]<m[sp];
			sp--;
			break;
		case GT:
			m[sp-1]=m[sp-1]>m[sp];
			sp--;
			break;
		case LE:
			m[sp-1]=m[sp-1]<=m[sp];
			sp--;
			break;
		case GE:
			m[sp-1]=m[sp-1]>=m[sp];
			sp--;
			break;
		case IN:
			cin>>m[++sp];
			break;
		case OUT:
			cout<<m[sp--]<<endl;
			break;
		case NOP:
			break;
		case LDC:
			m[++sp]=a1;
			break;
		case LDA:
			m[++sp]= d[a1]+a2;
			break;
		case LDV:
			m[++sp]=m[d[a1]+a2];
			break;
		case LDI:
			m[++sp]=m[m[d[a1]+a2]];
			break;
		case ST:
			m[d[a1]+a2]=m[sp--];
			break;
		case STI:
			m[m[d[a1]+a2]]=m[sp--];
			break;
		case JMP:
			pc=a1;
			break;
		case JMPF:
			if (!m[sp--]) pc=a1;
			break;
		case JMPT:
			if (m[sp--]) pc=a1;
			break;
		case INIT:
			sp=0;
			d[0]=0;
			break;
		case HALT:
			exit(0);
		case ALLOC:
			sp+=a1;
			break;
		case CALL:
			m[++sp]=pc;
 			pc=a1;
			break;
		case PROC:
			m[++sp]=d[a1];
			d[a1]=sp;
			if (a1>maxlevel) maxlevel=a1;	// for tracing purposes only
			break;
		case RET:
			d[a1]=m[sp--];
			pc=m[sp--];
			break;
		case ENTER:
			m[++sp]=d[a1];
			d[a1]=sp;
			if (a1>maxlevel) maxlevel=a1;	// for tracing purposes only
			break;
		case EXIT:
			d[a1]=m[sp--];
			break;
		case DUP:
			m[sp+1]=m[sp];
			sp++;
			break;
		case POP:
			sp--;
			break;
		case STL:
			m[m[sp-1]]=m[sp];
			sp-=2;
			break;
		case IND:
			m[sp]=m[m[sp]];
			break;
		case INCH:
			m[++sp]=cin.get( );
			break;
		case OUTCH:
			cout.put((char)(m[sp--]));
			break;
		case OUTSTR:
			for (int k=0; k<a1; k++) cout.put((char)(m[sp--]));
			cout<<endl;
			break;
		}
	}
}
