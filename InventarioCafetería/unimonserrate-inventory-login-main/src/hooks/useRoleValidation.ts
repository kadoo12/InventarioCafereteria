import { useState } from "react";

export const useRoleValidation = () => {
  const [showAlert, setShowAlert] = useState(false);

  const canPerformAction = (requiredRole: string | string[]): boolean => {
    const rolGuardado = localStorage.getItem("rol");
    
    if (Array.isArray(requiredRole)) {
      return requiredRole.includes(rolGuardado || "");
    }
    
    return rolGuardado === requiredRole;
  };

  const handleUnauthorized = () => {
    setShowAlert(true);
    setTimeout(() => setShowAlert(false), 3000);
  };

  return { canPerformAction, handleUnauthorized, showAlert };
};
