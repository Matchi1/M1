OUTPUT = notes
DIR = $(shell basename $(CURDIR))
REPORT_NAME = CHAN_MATHIEU_$(DIR)

all: zip

.SILENT:
report: $(OUTPUT).md
	$(info Generate pdf report from '$<' file)
	pandoc -s -o $(REPORT_NAME).pdf $< --highlight-style=espresso

.SILENT:
$(OUTPUT).md: $(OUTPUT).rmd
	Rscript -e "library(knitr); knit('$<')"

.SILENT:
clean:
	rm -f *.md
	rm -rf $(REPORT_NAME) $(REPORT_NAME)/

.SILENT:
mrproper: clean
	rm -f $(REPORT_NAME).*

.SILENT:
zip: clean
	$(info Create '$(REPORT_NAME)' directory)
	mkdir $(REPORT_NAME)
	cp -r src $(REPORT_NAME)/src
	$(info Zip '$(REPORT_NAME)' directory)
	zip -r $(REPORT_NAME).zip $(REPORT_NAME)
	make clean

.SILENT:
fullzip: report clean
	$(info Create '$(REPORT_NAME)' directory)
	mkdir $(REPORT_NAME)
	cp $(REPORT_NAME).pdf $(REPORT_NAME)
	cp -r src $(REPORT_NAME)/src
	$(info Zip '$(REPORT_NAME)' directory)
	zip -r $(REPORT_NAME).zip $(REPORT_NAME)
	make clean
