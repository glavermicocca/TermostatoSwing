# TermostatoSwing

Il solito progettino -> fiere MiniMakers

Compilato :

	gradle jar (testato con openjdk 1.8 su raspberry 3 -> Archinux)

Installato :

	pacman -S ttf-roboto

Utilizzato :

	TFT Touch 3.2 (per risolvere problema touch coordinata Y invertita)

	xinput set-prop 'ADS7846 Touchscreen' 'Coordinate Transformation Matrix' 1 0 0 0 -1 1 0 0 1
	
