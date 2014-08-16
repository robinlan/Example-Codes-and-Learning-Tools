all:
	make -f sin_table.mk all
	make -C cordic all
	make -C fixmath all

clean:
	make -f sin_table.mk clean
	make -C cordic clean
	make -C fixmath clean

install: all
	make -f sin_table.mk install
	make -C cordic install
	make -C fixmath install

