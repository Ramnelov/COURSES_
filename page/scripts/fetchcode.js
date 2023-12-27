function fetchCode(user, repo, path) {
    const url = `https://api.github.com/repos/${user}/${repo}/contents/${path}`;
  
    return fetch(url)
      .then(response => response.json())
      .then(data => {
        // The content of the file is base64 encoded, so we need to decode it
        const decodedData = atob(data.content);
        return decodedData;
      });
  }