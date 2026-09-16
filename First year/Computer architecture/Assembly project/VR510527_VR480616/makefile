all: bin/pianificatore

obj/pianificatore.o: src/pianificatore.s
	as --32 -gstabs src/pianificatore.s -o obj/pianificatore.o

bin/pianificatore: obj/pianificatore.o
	ld -m elf_i386 obj/pianificatore.o -o bin/pianificatore

clean:
	rm -f obj/pianificatore.o bin/pianificatore