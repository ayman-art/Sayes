const URL = "http://localhost:8080"
export const loginUser = async (name: string, password: string, role: string) => {
    const response = await fetch(`${URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name,
        "password":password,
        "role":role 
      }),
    });
  
    if (response.ok) {
      const token = response.headers.get("Authorization") ?? "";
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', token);
    } else {
      throw new Error('Login failed');
    }
  };
  
  export const registerDriver = async (
    name: string,
    password: string,
    role: string,
    plateNumber: string,
    licenseNumber: string
  ) => {
    const response = await fetch(`${URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name, 
        "password":password,
        "role":role,
        "plateNumber": plateNumber,
        "licenseNumber": licenseNumber 
      }),
    });
  
    if (response.ok ) {
      const token = response.headers.get("Authorization") ?? "";
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', token);
    } else {
      throw new Error('Registration failed');
    }
  };

  export const registerLotManager = async (name: string, password: string, role: string) => {
    const response = await fetch(`${URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name,
        "password":password,
        "role":role 
      }),
    });
  
    
    if (response.ok ) {
      // Save JWT to localStorage
      const token = response.headers.get("Authorization") ?? "";
      localStorage.setItem('jwtToken', token);
    } else {
      throw new Error('Registration failed');
    }

  };

  
  // Utility to check if the user is authenticated by validating the JWT
  export const getToken = (): string | null => {
    return localStorage.getItem('jwtToken');
  };
  
  export const logoutUser = () => {
    // Remove the JWT from localStorage
    localStorage.removeItem('jwtToken');
  };
  