function handleBlur(currentElement) {
    // Get the parent div with name="httpHeaders"
    var httpHeadersDiv = currentElement.closest('div[name="httpHeaders"]');
    if (httpHeadersDiv) {
        // Get the value of the current headerKey input and convert it to lowercase
        var headerKeyValue = currentElement.value;
        // Check if the headerKey value is not empty and is "password"
        if (headerKeyValue && headerKeyValue.toLowerCase() === 'password') {
            // Find the input with name="headerValue" within the div
            var headerValueInput = httpHeadersDiv.querySelector('input[name="headerValue"]');
            if (headerValueInput) {
                // Change the type to password
                headerValueInput.type = 'password';
            }
        }
    }
}
