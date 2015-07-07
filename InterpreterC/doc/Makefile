
TARGET	= manual

default: $(TARGET).pdf move clean

$(TARGET).pdf: $(TARGET).tex
	pdflatex $(TARGET).tex;
	pdflatex $(TARGET).tex;

move: $(TARGET).pdf
	mv $(TARGET).pdf ../

clean:
	rm -f $(TARGET).aux
	rm -f $(TARGET).out    
	rm -f $(TARGET).synctex.gz    
	rm -f $(TARGET).toc    
	rm -f $(TARGET).log    
