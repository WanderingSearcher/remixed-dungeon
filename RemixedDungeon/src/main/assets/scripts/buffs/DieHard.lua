---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by mike.
--- DateTime: 25.03.19 0:06
---

local buff = require "scripts/lib/buff"


return buff.init{
    desc  = function ()
        return {
            icon         = 44,
            name          = "DieHard_Name",
            info          = "DieHard_Info",
        }
    end,
    act = function(self,buff)
        buff:detach()
    end
}
