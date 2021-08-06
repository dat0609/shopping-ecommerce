var buttonLoad;
var dropDownContries;
var buttonAddCountry;
var buttonDeleteCountry;
var buttonUpdateCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries")
	dropDownContries = $("#dropDownCountries")
	buttonAddCountry = $("#buttonAddCountry")
	buttonDeleteCountry = $("#buttonDeleteCountry")
	buttonUpdateCountry = $("#buttonUpdateCountry")
	labelCountryName = $("#labelCountryName")
	fieldCountryName = $("#fieldCountryName")
	fieldCountryCode = $("#fieldCountryCode")

	buttonLoad.click(function() {
		loadCountries()
	})

	dropDownContries.on("change", function() {
		changeFormStateToSelectedCountry()
	})

	buttonAddCountry.click(function() {
		if (buttonAddCountry.val() == "Add") {
			addCountry()
		} else {
			changeFormStateToNewCountry()
		}
	})

	buttonUpdateCountry.click(function() {
		updateCountry();
	})

	buttonDeleteCountry.click(function() {
		deleteCountry();
	});
})

function deleteCountry() {
	optionValue = dropDownContries.val();
	countryId = optionValue.split("-")[0];

	url = contextPath + "countries/delete/" + countryId;

	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNewCountry();
		showToastMessage("The country has been deleted");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function updateCountry() {
	
	if(!validateFormCountry) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();

	countryId = dropDownContries.val().split("-")[0];

	jsonData = { id: countryId, name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");

		changeFormStateToNewCountry();
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function validateFormCountry() {
	formCountry = document.getElementById("formCountry")
	if (!formCountry.checkValidity()) {
		formCountry.reportValidity()
		return false;
	}
	
	return true
}

function addCountry() {
	
	if(!validateFormCountry) return;
	
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { name: countryName, code: countryCode };

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function() {
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});

}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);

	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry() {
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");

	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.prop("value", "New")
	buttonDeleteCountry.prop("disabled", false)
	buttonUpdateCountry.prop("disabled", false)

	labelCountryName.text("Selected Country:")


	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName)

	countryCode = dropDownContries.val().split("-")[1]
	fieldCountryCode.val(countryCode)

}

function loadCountries() {
	url = contextPath + "countries/list"
	$.get(url, function(responseJSON) {
		dropDownContries.empty()

		$.each(responseJSON, function(index, country) {
			optionValue = country.id + "-" + country.code
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownContries)
		})
	}).done(function() {
		buttonLoad.val("Refesh country list")
		showToastMessage("Loaded")
	}).fail(function() {
		showToastMessage("ERROR from server")
	})
}

function showToastMessage(message) {
	$("#toastMessage").text(message)
	$(".toast").toast('show')
}