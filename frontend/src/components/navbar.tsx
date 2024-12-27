import React from 'react';
interface TopNavProps {
    onLogout: () => void;
}

const TopNav: React.FC<TopNavProps> = ({ onLogout }) => {
    return (
        <nav className="fixed top-0 w-full bg-white shadow-md p-4 z-50">
            <div className="flex justify-end">
                <button
                    className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition-colors"
                    onClick={onLogout}
                >
                    Logout
                </button>
            </div>
        </nav>
    );
};

export default TopNav;
