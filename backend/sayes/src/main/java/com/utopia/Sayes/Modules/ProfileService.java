package com.utopia.Sayes.Modules;

import com.utopia.Sayes.Repo.LotDAO;
import com.utopia.Sayes.Repo.SpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    LotDAO lotDAO;
    @Autowired
    SpotDAO spotDAO;
}
