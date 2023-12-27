
// Define the files and line ranges you're interested in
const files = [
  { id: 'first-code-snippet', path: './index.html', startLine: 1, endLine: 10 },
  { id: 'second-code-snippet', path: './application/backend/pom.xml', startLine: 1, endLine: 20 },
  { id: 'third-code-snippet', path: './application/backend/src/main/java/com/ramnelov/backend/controller/UserController.java', startLine: 40, endLine: 60 },
  // Add more files as needed
];

window.onload = function() {
  // Loop over the files
  for (const file of files) {
    // Find the element where the code will be displayed
    const display = document.getElementById(file.id);

    // Fetch and display the code
    fetchCode(file.path, file.startLine, file.endLine)
      .then(lines => {
        // Join the lines with a line break to display them separately
        display.textContent = lines.join('\n');
      });
  }
};

function fetchCode(path, startLine, endLine) {
    const url = `https://api.github.com/repos/ramnelov/user-application/contents/${path}`;
  
    return fetch(url)
    .then(response => response.json())
    .then(data => {
      // The content of the file is base64 encoded, so we need to decode it
      const decodedData = atob(data.content);
      // Split the decoded data into lines
      const lines = decodedData.split('\n');
      // Extract the lines in the specified range
      const selectedLines = lines.slice(startLine - 1, endLine);
      // Join the selected lines back into a string
      return selectedLines;
    });
  }