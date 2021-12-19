OUTPUT = notes
DIR = $(shell basename $(CURDIR))
REPORT_NAME = CHAN_MATHIEU_$(DIR)

.SILENT:
report: $(OUTPUT).md
	pandoc -s -o $(REPORT_NAME).pdf $< --highlight-style=espresso

.SILENT:
$(OUTPUT).md: $(OUTPUT).rmd
	Rscript -e "library(knitr); knit('$<')"

.SILENT:
clean:
	rm -f *.md
	rm -rf $(REPORT_NAME)

.SILENT:
mrproper: clean
	rm -f $(REPORT_NAME).*

.SILENT:
zip: report clean
	mkdir $(REPORT_NAME)
	cp -r src $(REPORT_NAME)/src
	cp $(REPORT_NAME).pdf $(REPORT_NAME)
	zip -r $(REPORT_NAME).zip $(REPORT_NAME)
